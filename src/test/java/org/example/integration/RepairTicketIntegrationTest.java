package org.example.integration;

import org.example.domainEntities.*;
import org.example.dto.WorkerRepairTicketRequest;
import org.example.dto.WorkerRepairTicketResponse;
import org.example.repository.*;
import org.example.service.RepairTicketService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class RepairTicketIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16")
                    .withDatabaseName("equip_test")
                    .withUsername("test")
                    .withPassword("test");

    @DynamicPropertySource
    static void configureDatabase(DynamicPropertyRegistry registry) {
        registry.add("DB_URL", postgres::getJdbcUrl);
        registry.add("DB_USERNAME", postgres::getUsername);
        registry.add("DB_PASSWORD", postgres::getPassword);
    }

    @Autowired
    private RepairTicketService repairTicketService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EquipmentCategoryRepository categoryRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private RepairTicketRepository ticketRepository;

    @Autowired
    private ApplicationContext context;

    private User worker;
    private Equipment equipment;

    @BeforeEach
    void setUp() {

        String uniqueId = UUID.randomUUID()
                .toString()
                .substring(0, 8);

        // Reuse department if it already exists
        Department department = departmentRepository.findAll()
                .stream()
                .filter(d -> d.getName().equals("Maintenance"))
                .findFirst()
                .orElseGet(() -> departmentRepository.save(
                        new Department(
                                null,
                                "Maintenance",
                                "Maintenance department"
                        )
                ));

        // Reuse equipment category if it already exists
        EquipmentCategory category = categoryRepository.findAll()
                .stream()
                .filter(c -> c.getName().equals("Tractor"))
                .findFirst()
                .orElseGet(() -> categoryRepository.save(
                        new EquipmentCategory(
                                null,
                                "Tractor",
                                "Agricultural equipment"
                        )
                ));

        // Reuse location if it already exists
        Location location = locationRepository.findAll()
                .stream()
                .filter(l -> l.getName().equals("Workshop"))
                .findFirst()
                .orElseGet(() -> locationRepository.save(
                        new Location(
                                null,
                                "Workshop",
                                Location.LocationType.WORKSHOP,
                                "Main workshop"
                        )
                ));

        // Create a unique worker for each test
        worker = new User();
        worker.setDepartment(department);
        worker.setName("Test Worker " + uniqueId);
        worker.setEmail("worker-" + uniqueId + "@test.com");
        worker.setPasswordHash("test-password");
        worker.setRole(User.UserRole.OPERATOR);
        worker.setActive(true);

        worker = userRepository.save(worker);

        // Create unique equipment for each test
        equipment = Equipment.builder()
                .equipmentCategory(category)
                .location(location)
                .name("Test Tractor " + uniqueId)
                .serialNumber("TRACTOR-" + uniqueId)
                .manufacturer("Test Manufacturer")
                .status(Equipment.EquipmentStatus.ASSIGNED)
                .build();

        equipment = equipmentRepository.save(equipment);

        // Assign equipment to worker
        Assignment assignment = Assignment.builder()
                .user(worker)
                .equipment(equipment)
                .startDate(Instant.now().minusSeconds(3600))
                .build();

        assignmentRepository.save(assignment);

        // Authenticate worker
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        worker.getEmail(),
                        null,
                        List.of(
                                new SimpleGrantedAuthority("ROLE_OPERATOR")
                        )
                )
        );
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldCreateRepairTicketInPostgres() {

        WorkerRepairTicketRequest request =
                new WorkerRepairTicketRequest(
                        equipment.getEquipmentId(),
                        "Engine overheating",
                        RepairTicket.RepairPriority.HIGH
                );

        WorkerRepairTicketResponse response =
                repairTicketService.createRepairTicket(request);

        assertEquals("200", response.getStatusCode());

        assertNotNull(response.getRepairTicketId());

        RepairTicket savedTicket =
                ticketRepository.findById(response.getRepairTicketId())
                        .orElseThrow();

        assertEquals(
                "Engine overheating",
                savedTicket.getDescription()
        );

        assertEquals(
                RepairTicket.RepairPriority.HIGH,
                savedTicket.getPriority()
        );

        assertEquals(
                RepairTicket.RepairStatus.OPEN,
                savedTicket.getStatus()
        );

        assertEquals(
                worker.getUserId(),
                savedTicket.getReportedBy().getUserId()
        );

        assertEquals(
                equipment.getEquipmentId(),
                savedTicket.getEquipment().getEquipmentId()
        );

        assertEquals(
                1,
                ticketRepository.countByEquipment_EquipmentId(
                        equipment.getEquipmentId()
                )
        );
    }

    @Test
    void shouldConnectToRealPostgresDatabase() {

        String databaseName = jdbcTemplate.queryForObject(
                "SELECT current_database()",
                String.class
        );

        assertEquals("equip_test", databaseName);
    }

    @Test
    void shouldLoadRepairTicketService() {

        assertNotNull(
                context.getBean(RepairTicketService.class)
        );
    }
}
