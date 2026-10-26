package org.example.service;

import org.example.domainEntities.Assignment;
import org.example.domainEntities.Equipment;
import org.example.domainEntities.RepairTicket;
import org.example.domainEntities.User;
import org.example.dto.MechanicRepairTicketResp;
import org.example.dto.WorkerRepairTicketRequest;
import org.example.dto.WorkerRepairTicketResponse;
import org.example.dto.GetAllRepairTickets;
import org.example.dto.CompleteRepairTicketReq;
import org.example.dto.CompleteRepairTicketResp;

import org.example.mapper.RepairTicketMapper;
import org.example.repository.AssignmentRepository;
import org.example.repository.EquipmentRepository;
import org.example.repository.RepairTicketRepository;
import org.example.repository.UserRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.List;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RepairTicketServiceTest {

    @Mock
    private RepairTicketRepository ticketRepo;

    @Mock
    private UserRepository userRepo;

    @Mock
    private EquipmentRepository equipRepo;

    @Mock
    private AssignmentRepository assignRepo;

    @Mock
    private RepairTicketMapper mapper;

    @InjectMocks
    private RepairTicketService service;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    private void authenticateAs(String email) {
        var authentication =
                new UsernamePasswordAuthenticationToken(
                        email, "password");

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);
    }

    @Test
    void shouldReturnErrorWhenUserDoesNotExist() {
        // Arrange
        authenticateAs("worker@example.com");

        WorkerRepairTicketRequest request =
                mock(WorkerRepairTicketRequest.class);

        when(userRepo.findByEmail("worker@example.com"))
                .thenReturn(null);

        // Act
        WorkerRepairTicketResponse response =
                service.createRepairTicket(request);

        // Assert
        assertEquals("User not found", response.getMessage());

        verifyNoInteractions(equipRepo, assignRepo);
        verify(ticketRepo, never()).save(any());
    }

    @Test
    void shouldReturnErrorWhenEquipmentDoesNotExist() {
        authenticateAs("worker@example.com");

        Long equipmentId = 10L;
        WorkerRepairTicketRequest request =
                mock(WorkerRepairTicketRequest.class);

        User user = mock(User.class);

        when(request.getId()).thenReturn(equipmentId);
        when(userRepo.findByEmail("worker@example.com"))
                .thenReturn(user);
        when(user.getUserId()).thenReturn(1L);
        when(equipRepo.findById(equipmentId))
                .thenReturn(Optional.empty());

        WorkerRepairTicketResponse response =
                service.createRepairTicket(request);

        assertEquals("Equipment not found", response.getMessage());

        verify(assignRepo, never())
                .findActiveAsignment(anyLong(), anyLong(), any());
        verify(ticketRepo, never()).save(any(RepairTicket.class));
    }

    @Test
    void shouldReturnErrorWhenEquipmentIsNotActivelyAssigned() {
        authenticateAs("worker@example.com");

        Long equipmentId = 10L;

        WorkerRepairTicketRequest request =
                mock(WorkerRepairTicketRequest.class);
        User user = mock(User.class);
        Equipment equipment = mock(Equipment.class);

        when(request.getId()).thenReturn(equipmentId);
        when(userRepo.findByEmail("worker@example.com"))
                .thenReturn(user);
        when(user.getUserId()).thenReturn(1L);

        when(equipRepo.findById(equipmentId))
                .thenReturn(Optional.of(equipment));
        when(equipment.getEquipmentId()).thenReturn(equipmentId);

        when(assignRepo.findActiveAsignment(
                eq(1L), eq(equipmentId), any(Instant.class)))
                .thenReturn(Optional.empty());

        WorkerRepairTicketResponse response =
                service.createRepairTicket(request);

        assertEquals(
                "Equipment not actively assigned to worker",
                response.getMessage());

        verify(ticketRepo, never()).save(any(RepairTicket.class));
        verify(ticketRepo, never())
                .existsByEquipment_EquipmentIdAndStatusIn(
                        anyLong(), anyList());
    }

    @Test
    void shouldReturnConflictWhenTicketAlreadyExists() {
        authenticateAs("worker@example.com");

        Long equipmentId = 10L;

        WorkerRepairTicketRequest request =
                mock(WorkerRepairTicketRequest.class);
        User user = mock(User.class);
        Equipment equipment = mock(Equipment.class);
        Assignment assignment = mock(Assignment.class);

        when(request.getId()).thenReturn(equipmentId);
        when(userRepo.findByEmail("worker@example.com"))
                .thenReturn(user);
        when(user.getUserId()).thenReturn(1L);

        when(equipRepo.findById(equipmentId))
                .thenReturn(Optional.of(equipment));
        when(equipment.getEquipmentId()).thenReturn(equipmentId);

        when(assignRepo.findActiveAsignment(
                eq(1L), eq(equipmentId), any(Instant.class)))
                .thenReturn(Optional.of(assignment));

        when(ticketRepo.existsByEquipment_EquipmentIdAndStatusIn(
                eq(equipmentId), anyList()))
                .thenReturn(true);

        WorkerRepairTicketResponse response =
                service.createRepairTicket(request);

        assertEquals("409", response.getStatusCode());
        assertEquals(
                "A ticket already exists for this equipment",
                response.getMessage());

        verify(ticketRepo, never()).save(any(RepairTicket.class));
    }

    @Test
    void shouldCreateTicketWhenAllChecksPass() {
        // Arrange
        authenticateAs("worker@example.com");

        Long equipmentId = 10L;

        WorkerRepairTicketRequest request =
                mock(WorkerRepairTicketRequest.class);

        User user = mock(User.class);
        Equipment equipment = mock(Equipment.class);
        Assignment assignment = mock(Assignment.class);

        when(request.getId()).thenReturn(equipmentId);
        when(request.getDescription())
                .thenReturn("Engine needs repair");

        when(userRepo.findByEmail("worker@example.com"))
                .thenReturn(user);
        when(user.getUserId()).thenReturn(1L);

        when(equipRepo.findById(equipmentId))
                .thenReturn(Optional.of(equipment));
        when(equipment.getEquipmentId())
                .thenReturn(equipmentId);

        when(assignRepo.findActiveAsignment(
                eq(1L), eq(equipmentId), any(Instant.class)))
                .thenReturn(Optional.of(assignment));

        when(ticketRepo
                .existsByEquipment_EquipmentIdAndStatusIn(
                        eq(equipmentId), anyList()))
                .thenReturn(false);

        when(ticketRepo.save(any(RepairTicket.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        WorkerRepairTicketResponse response =
                service.createRepairTicket(request);

        // Assert
        assertEquals("200", response.getStatusCode());
        assertEquals(
                "Repair ticket created successfully",
                response.getMessage());
        assertEquals("OPEN", response.getTicketState());

        verify(ticketRepo).save(any(RepairTicket.class));
    }

    @Test
    void shouldReturn404WhenTicketDoesNotExist() {
        Long ticketId = 100L;

        when(ticketRepo.findById(ticketId))
                .thenReturn(Optional.empty());

        MechanicRepairTicketResp response =
                service.mechanicTakesTicket(ticketId);

        assertEquals("404", response.getStatusCode());
        assertEquals("Ticket not found", response.getMessage());

        verify(ticketRepo, never()).save(any(RepairTicket.class));
        verifyNoInteractions(userRepo);
    }

    @Test
    void shouldReturn409WhenTicketIsNotOpen() {
        Long ticketId = 100L;

        RepairTicket ticket = mock(RepairTicket.class);

        when(ticketRepo.findById(ticketId))
                .thenReturn(Optional.of(ticket));
        when(ticket.getStatus())
                .thenReturn(RepairTicket.RepairStatus.IN_PROGRESS);

        MechanicRepairTicketResp response =
                service.mechanicTakesTicket(ticketId);

        assertEquals("409", response.getStatusCode());
        assertEquals(
                "Ticket is not available for assignment",
                response.getMessage());

        verify(ticketRepo, never()).save(any(RepairTicket.class));
        verifyNoInteractions(userRepo);
    }

    @Test
    void shouldAssignOpenTicketToMechanic() {
        // Arrange
        authenticateAs("mechanic@example.com");

        Long ticketId = 100L;

        User mechanic = mock(User.class);
        User reporter = mock(User.class);
        Equipment equipment = mock(Equipment.class);

        RepairTicket ticket = RepairTicket.builder()
                .repairRequestId(ticketId)
                .equipment(equipment)
                .reportedBy(reporter)
                .technician(null)
                .description("Engine problem")
                .priority(RepairTicket.RepairPriority.HIGH)
                .status(RepairTicket.RepairStatus.OPEN)
                .build();

        when(ticketRepo.findById(ticketId))
                .thenReturn(Optional.of(ticket));

        when(userRepo.findByEmail("mechanic@example.com"))
                .thenReturn(mechanic);

        when(mechanic.getName()).thenReturn("John");
        when(reporter.getName()).thenReturn("Worker");
        when(equipment.getName()).thenReturn("Forklift");

        // Act
        MechanicRepairTicketResp response =
                service.mechanicTakesTicket(ticketId);

        // Assert
        assertEquals("200", response.getStatusCode());
        assertEquals(
                "Ticket assigned successfully to mechanic",
                response.getMessage());

        assertEquals(ticketId, response.getRepairRequestId());
        assertEquals("Forklift", response.getEquipmentName());
        assertEquals("Worker", response.getReportedBy());
        assertEquals("John", response.getTechnician());
        assertEquals("Engine problem", response.getDescription());
        assertEquals(
                RepairTicket.RepairPriority.HIGH,
                response.getPriority());

        // we verify the state of the object
        assertEquals(
                RepairTicket.RepairStatus.IN_PROGRESS,
                ticket.getStatus());

        assertSame(mechanic, ticket.getTechnician());

        verify(ticketRepo).findById(ticketId);
        verify(userRepo).findByEmail("mechanic@example.com");
        verify(ticketRepo).save(ticket);
    }

    @Test
    void shouldReturnOnlyTicketsReportedByOperator() {
        authenticateAs("worker@example.com");

        User operator = mock(User.class);

        when(userRepo.findByEmail("worker@example.com"))
                .thenReturn(operator);
        when(operator.getRole())
                .thenReturn(User.UserRole.OPERATOR);
        when(operator.getUserId()).thenReturn(1L);

        when(ticketRepo.findByReportedBy_UserId(1L))
                .thenReturn(List.of());

        List<GetAllRepairTickets> result =
                service.getRepairTickets(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(ticketRepo).findByReportedBy_UserId(1L);
        verify(ticketRepo, never()).findAll();
        verify(ticketRepo, never()).findByStatusIn(anyList());
    }

    @Test
    void shouldReturnOpenTicketsByDefaultForMechanic() {
        authenticateAs("mechanic@example.com");

        User mechanic = mock(User.class);

        when(userRepo.findByEmail("mechanic@example.com"))
                .thenReturn(mechanic);
        when(mechanic.getRole())
                .thenReturn(User.UserRole.MECHANIC);
        when(ticketRepo.findByStatusIn(
                List.of(RepairTicket.RepairStatus.OPEN)))
                .thenReturn(List.of());

        List<GetAllRepairTickets> result =
                service.getRepairTickets(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(ticketRepo).findByStatusIn(
                List.of(RepairTicket.RepairStatus.OPEN));
        verify(ticketRepo, never()).findAll();
    }

    @Test
    void shouldReturn404WhenFinalizingNonexistentTicket() {
        // Arrange
        Long ticketId = 100L;

        CompleteRepairTicketReq request =
                mock(CompleteRepairTicketReq.class);

        when(request.getTicketId()).thenReturn(ticketId);
        when(ticketRepo.findById(ticketId))
                .thenReturn(Optional.empty());

        // Act
        CompleteRepairTicketResp response =
                service.mechanicFinalizesTicket(request);

        // Assert
        assertEquals("404", response.getStatusCode());
        assertEquals("Ticket doesn't exist", response.getMessage());

        verify(ticketRepo).findById(ticketId);
        verify(ticketRepo, never()).save(any(RepairTicket.class));
        verifyNoInteractions(userRepo);
    }

    @Test
    void shouldReturn403WhenTechnicianIsNotAssignedToTicket() {
        // Arrange
        authenticateAs("other@example.com");

        Long ticketId = 100L;

        CompleteRepairTicketReq request =
                mock(CompleteRepairTicketReq.class);

        User technician = mock(User.class);

        RepairTicket ticket = mock(RepairTicket.class);

        when(request.getTicketId()).thenReturn(ticketId);
        when(ticketRepo.findById(ticketId))
                .thenReturn(Optional.of(ticket));

        when(ticket.getTechnician()).thenReturn(technician);
        when(technician.getEmail())
                .thenReturn("assigned@example.com");

        // Act
        CompleteRepairTicketResp response =
                service.mechanicFinalizesTicket(request);

        // Assert
        assertEquals("403", response.getStatusCode());
        assertEquals(
                "Technician not assigned to ticket",
                response.getMessage()
        );

        verify(ticketRepo, never()).save(any(RepairTicket.class));
    }

    @Test
    void shouldReturn409WhenTicketIsNotInProgress() {
        // Arrange
        authenticateAs("mechanic@example.com");

        Long ticketId = 100L;

        CompleteRepairTicketReq request =
                mock(CompleteRepairTicketReq.class);

        User technician = mock(User.class);
        RepairTicket ticket = mock(RepairTicket.class);

        when(request.getTicketId()).thenReturn(ticketId);

        when(ticketRepo.findById(ticketId))
                .thenReturn(Optional.of(ticket));

        when(ticket.getTechnician()).thenReturn(technician);
        when(technician.getEmail())
                .thenReturn("mechanic@example.com");

        when(ticket.getStatus())
                .thenReturn(RepairTicket.RepairStatus.OPEN);

        // Act
        CompleteRepairTicketResp response =
                service.mechanicFinalizesTicket(request);

        // Assert
        assertEquals("409", response.getStatusCode());
        assertEquals(
                "Only tickets in progress can be finalized",
                response.getMessage()
        );

        verify(ticketRepo, never()).save(any(RepairTicket.class));
    }

    @Test
    void shouldFinalizeTicketWhenTechnicianIsAssignedAndTicketIsInProgress() {
        authenticateAs("mechanic@example.com");

        Long ticketId = 100L;
        BigDecimal cost = new BigDecimal("250.50");

        CompleteRepairTicketReq request =
                mock(CompleteRepairTicketReq.class);

        User technician = mock(User.class);
        User reporter = mock(User.class);
        Equipment equipment = mock(Equipment.class);

        RepairTicket ticket = RepairTicket.builder()
                .repairRequestId(ticketId)
                .technician(technician)
                .reportedBy(reporter)
                .equipment(equipment)
                .description("Engine problem")
                .priority(RepairTicket.RepairPriority.HIGH)
                .status(RepairTicket.RepairStatus.IN_PROGRESS)
                .build();

        when(request.getTicketId()).thenReturn(ticketId);
        when(request.getStatus())
                .thenReturn(RepairTicket.RepairStatus.RESOLVED);
        when(request.getCost()).thenReturn(cost);

        when(ticketRepo.findById(ticketId))
                .thenReturn(Optional.of(ticket));

        when(technician.getEmail())
                .thenReturn("mechanic@example.com");
        when(technician.getName()).thenReturn("John");

        when(reporter.getName()).thenReturn("Worker");
        when(equipment.getName()).thenReturn("Forklift");

        // act
        CompleteRepairTicketResp response =
                service.mechanicFinalizesTicket(request);

        // response
        assertEquals("200", response.getStatusCode());
        assertEquals(
                "Ticket updated successfully",
                response.getMessage()
        );

        assertEquals(ticketId, response.getRepairTicketId());
        assertEquals("Forklift", response.getEquipmentName());
        assertEquals("Worker", response.getReportedBy());
        assertEquals("John", response.getTechnician());
        assertEquals("Engine problem", response.getDescription());
        assertEquals(
                RepairTicket.RepairPriority.HIGH,
                response.getPriority()
        );

        assertEquals(
                RepairTicket.RepairStatus.RESOLVED,
                response.getStatus()
        );
        assertEquals(cost, response.getCost());
        assertNotNull(response.getResolvedAt());

        // ticket was modified
        assertEquals(
                RepairTicket.RepairStatus.RESOLVED,
                ticket.getStatus()
        );
        assertEquals(cost, ticket.getCost());
        assertNotNull(ticket.getResolvedAt());

        // assert: repository
        verify(ticketRepo).findById(ticketId);
        verify(ticketRepo).save(ticket);
    }
}
