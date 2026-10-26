package org.example.service;

import org.example.domainEntities.Assignment;
import org.example.domainEntities.Equipment;
import org.example.domainEntities.User;
import org.example.dto.AssignEquipmentReq;
import org.example.dto.AssignEquipmentResp;
import org.example.repository.AssignmentRepository;
import org.example.repository.EquipmentRepository;
import org.example.repository.UserRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssignEquipmentServiceTest {

    @Mock
    private AssignmentRepository assignRepo;

    @Mock
    private UserRepository userRepo;

    @Mock
    private EquipmentRepository equipRepo;

    @InjectMocks
    private AssignEquipmentService assignEquipmentService;

    private User manager;
    private User worker;
    private Equipment equipment;
    private AssignEquipmentReq request;

    private final Instant startDate =
            Instant.parse("2026-11-01T10:00:00Z");

    private final Instant endDate =
            Instant.parse("2026-11-10T10:00:00Z");

    @BeforeEach
    void setUp() {
        manager = mock(User.class);
        worker = mock(User.class);
        equipment = mock(Equipment.class);

        request = AssignEquipmentReq.builder()
                .equipmentId(10L)
                .userId(2L)
                .startDate(startDate)
                .endDate(endDate)
                .notes("Assignment test")
                .build();

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "manager@example.com",
                        "password"
                )
        );
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private void authenticateAsManager() {
        when(userRepo.findByEmail("manager@example.com"))
                .thenReturn(manager);
        when(manager.getRole())
                .thenReturn(User.UserRole.MANAGER);
    }

    private void prepareAvailableEquipment() {
        when(equipRepo.findById(10L))
                .thenReturn(Optional.of(equipment));
        when(equipment.getStatus())
                .thenReturn(Equipment.EquipmentStatus.AVAILABLE);
    }

    private void prepareActiveWorker() {
        when(userRepo.findById(2L))
                .thenReturn(Optional.of(worker));
        when(worker.isActive()).thenReturn(true);
        when(worker.getUserId()).thenReturn(2L);
        when(worker.getName()).thenReturn("Ion Popescu");
    }

    @Test
    void shouldReturn400WhenStartDateIsAfterEndDate() {
        request = AssignEquipmentReq.builder()
                .equipmentId(10L)
                .userId(2L)
                .startDate(endDate)
                .endDate(startDate)
                .build();

        AssignEquipmentResp response =
                assignEquipmentService.assignEquipment(request);

        assertEquals("400", response.getStatusCode());
        assertEquals(
                "Start date cannot be grater than end date",
                response.getMessage()
        );

        verifyNoInteractions(assignRepo, userRepo, equipRepo);
    }

    @Test
    void shouldReturn400WhenStartDateEqualsEndDate() {
        request = AssignEquipmentReq.builder()
                .equipmentId(10L)
                .userId(2L)
                .startDate(startDate)
                .endDate(startDate)
                .build();

        AssignEquipmentResp response =
                assignEquipmentService.assignEquipment(request);

        assertEquals("400", response.getStatusCode());
        assertEquals(
                "Start date cannot be equal with end date",
                response.getMessage()
        );

        verifyNoInteractions(assignRepo, userRepo, equipRepo);
    }

    @Test
    void shouldReturn403WhenUserIsNotManager() {
        when(userRepo.findByEmail("manager@example.com"))
                .thenReturn(manager);
        when(manager.getRole())
                .thenReturn(User.UserRole.OPERATOR);

        AssignEquipmentResp response =
                assignEquipmentService.assignEquipment(request);

        assertEquals("403", response.getStatusCode());
        assertEquals("Not allowed to assign", response.getMessage());

        verify(userRepo).findByEmail("manager@example.com");
        verify(userRepo, never()).findById(any());
        verifyNoInteractions(assignRepo, equipRepo);
    }

    @Test
    void shouldReturn404WhenEquipmentDoesNotExist() {
        authenticateAsManager();

        when(equipRepo.findById(10L))
                .thenReturn(Optional.empty());

        AssignEquipmentResp response =
                assignEquipmentService.assignEquipment(request);

        assertEquals("404", response.getStatusCode());
        assertEquals("Equipment not found", response.getMessage());

        verify(equipRepo).findById(10L);
        verify(userRepo, never()).findById(any());
        verifyNoInteractions(assignRepo);
    }

    @Test
    void shouldReturn409WhenEquipmentIsNotAvailable() {
        authenticateAsManager();

        when(equipRepo.findById(10L))
                .thenReturn(Optional.of(equipment));
        when(equipment.getStatus())
                .thenReturn(Equipment.EquipmentStatus.ASSIGNED);

        AssignEquipmentResp response =
                assignEquipmentService.assignEquipment(request);

        assertEquals("409", response.getStatusCode());
        assertEquals(
                "Equipment not currently available for assignation",
                response.getMessage()
        );

        verify(userRepo, never()).findById(any());
        verifyNoInteractions(assignRepo);
        verify(equipRepo, never()).save(any());
    }

    @Test
    void shouldReturn404WhenWorkerDoesNotExist() {
        authenticateAsManager();
        prepareAvailableEquipment();

        when(userRepo.findById(2L))
                .thenReturn(Optional.empty());

        AssignEquipmentResp response =
                assignEquipmentService.assignEquipment(request);

        assertEquals("404", response.getStatusCode());
        assertEquals("Worker not found", response.getMessage());

        verify(userRepo).findById(2L);
        verifyNoInteractions(assignRepo);
        verify(equipRepo, never()).save(any());
    }

    @Test
    void shouldReturn404WhenWorkerIsInactive() {
        authenticateAsManager();
        prepareAvailableEquipment();

        when(userRepo.findById(2L))
                .thenReturn(Optional.of(worker));
        when(worker.isActive()).thenReturn(false);

        AssignEquipmentResp response =
                assignEquipmentService.assignEquipment(request);

        assertEquals("404", response.getStatusCode());
        assertEquals(
                "Worker not available for assignment",
                response.getMessage()
        );

        verifyNoInteractions(assignRepo);
        verify(equipRepo, never()).save(any());
    }

    @Test
    void shouldCreateAssignmentAndUpdateEquipmentStatusWhenRequestIsValid() {
        authenticateAsManager();
        prepareAvailableEquipment();
        prepareActiveWorker();

        when(equipment.getEquipmentId()).thenReturn(10L);
        when(equipment.getName()).thenReturn("Laptop");

        when(assignRepo.save(any(Assignment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        AssignEquipmentResp response =
                assignEquipmentService.assignEquipment(request);

        //verify the response
        assertEquals("201", response.getStatusCode());
        assertEquals("Assignment created", response.getMessage());
        assertEquals(10L, response.getEquipmentId());
        assertEquals("Laptop", response.getEquipmentName());
        assertEquals(2L, response.getUserId());
        assertEquals("Ion Popescu", response.getWorkerName());
        assertEquals(startDate, response.getStartDate());
        assertEquals(endDate, response.getEndDate());

        // verify the saved assignment
        ArgumentCaptor<Assignment> captor =
                ArgumentCaptor.forClass(Assignment.class);

        verify(assignRepo).save(captor.capture());

        Assignment savedAssignment = captor.getValue();

        assertSame(equipment, savedAssignment.getEquipment());
        assertSame(worker, savedAssignment.getUser());
        assertEquals(startDate, savedAssignment.getStartDate());
        assertEquals(endDate, savedAssignment.getEndDate());
        assertEquals("Assignment test", savedAssignment.getNotes());

        verify(equipment).setStatus(
                Equipment.EquipmentStatus.ASSIGNED
        );
        verify(equipRepo).save(equipment);
    }
}
