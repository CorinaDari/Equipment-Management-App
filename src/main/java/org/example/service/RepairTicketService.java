package org.example.service;

import org.example.domainEntities.Assignment;
import org.example.domainEntities.Equipment;
import org.example.domainEntities.RepairTicket;
import org.example.domainEntities.User;
import org.example.dto.GetAllRepairTickets;
import org.example.dto.WorkerRepairTicketRequest;
import org.example.dto.WorkerRepairTicketResponse;
import org.example.mapper.RepairTicketMapper;
import org.example.repository.AssignmentRepository;
import org.example.repository.EquipmentRepository;
import org.example.repository.RepairTicketRepository;
import org.example.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.example.domainEntities.RepairTicket.RepairStatus.IN_PROGRESS;
import static org.example.domainEntities.RepairTicket.RepairStatus.OPEN;

@Service
public class RepairTicketService {

    private final RepairTicketRepository ticketRepo;
    private final UserRepository userRepo;
    private final EquipmentRepository equipRepo;
    private final AssignmentRepository assignRepo;
    private final RepairTicketMapper mapper;


    public RepairTicketService(RepairTicketRepository ticketRepo, UserRepository userRepo, EquipmentRepository equipRepo, AssignmentRepository assignRepo, RepairTicketMapper mapper) {
        this.ticketRepo = ticketRepo;
        this.userRepo = userRepo;
        this.equipRepo = equipRepo;
        this.assignRepo = assignRepo;
        this.mapper = mapper;
    }

    @PreAuthorize("hasAnyRole('OPERATOR')")
    public WorkerRepairTicketResponse createRepairTicket(WorkerRepairTicketRequest request){
        WorkerRepairTicketResponse response = new WorkerRepairTicketResponse();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();
        Long userId;
        User user;

        Optional<User> optionalUser = Optional.ofNullable(userRepo.findByEmail(username));
        if (optionalUser.isPresent()){
            user = optionalUser.get();
            userId = user.getUserId();
        }
        else{
            response.setMessage("User not found");
            return response;
        }

        Optional<Equipment> optionalEquipment = equipRepo.findById(request.getId());
        Long equipmentId;
        Equipment equipment;

        if(optionalEquipment.isPresent()){
            equipment = optionalEquipment.get();
            equipmentId = equipment.getEquipmentId();
        }
        else {
            response.setMessage("Equipment not found");
            return response;
        }

        Instant assignmentTime = Instant.now();
        Optional<Assignment> optionalAssignment = assignRepo.findActiveAsignment(userId, equipmentId, assignmentTime);

        if(optionalAssignment.isEmpty()){
            response.setMessage("Equipment not actively assigned to worker");
            return response;
        }

        boolean ticketAlreadyExists = ticketRepo.existsByEquipment_EquipmentIdAndStatusIn(request.getId(), List.of(OPEN, IN_PROGRESS));

        if(ticketAlreadyExists){
            response.setMessage("A ticket already exists for this equipment");
            response.setStatusCode("409");
            return response;
        }

        RepairTicket repairTicket =  RepairTicket.builder()
                .equipment(equipment)
                .description(request.getDescription())
                .reportedBy(user)
                .priority(request.getPriority())
                .status(OPEN)
                .build();

        ticketRepo.save(repairTicket);

        response.setRepairTicketId(repairTicket.getRepairRequestId());
        response.setStatusCode("200");
        response.setCreatedAt(repairTicket.getReportedAt());
        response.setTicketState(repairTicket.getStatus().name());
        response.setMessage("Repair ticket created successfully");

        return response;
    }

    public List<GetAllRepairTickets> getRepairTickets(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();
        User currentUser = userRepo.findByEmail(email);

        List<RepairTicket> tickets;

        if(currentUser.getRole() == User.UserRole.OPERATOR){
            tickets = ticketRepo.findByReportedBy_UserId(currentUser.getUserId());
        }
        else{
            tickets = ticketRepo.findAll();
        }

        return tickets.stream()
                .map(mapper::toDto)
                .toList();
    }
}
