package org.example.service;

import org.example.domainEntities.Assignment;
import org.example.domainEntities.Equipment;
import org.example.domainEntities.RepairTicket;
import org.example.domainEntities.User;
import org.example.dto.*;
import org.example.mapper.RepairTicketMapper;
import org.example.repository.AssignmentRepository;
import org.example.repository.EquipmentRepository;
import org.example.repository.RepairTicketRepository;
import org.example.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
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
            response.setMessage("404");
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
            response.setMessage("404");
            response.setMessage("Equipment not found");
            return response;
        }

        Instant assignmentTime = Instant.now();
        Optional<Assignment> optionalAssignment = assignRepo.findActiveAsignment(userId, equipmentId, assignmentTime);

        if(optionalAssignment.isEmpty()){
            response.setMessage("403");
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
        response.setStatusCode("201");
        response.setCreatedAt(repairTicket.getReportedAt());
        response.setTicketState(repairTicket.getStatus().name());
        response.setMessage("Repair ticket created successfully");

        return response;
    }

    public List<GetAllRepairTickets> getRepairTickets(
            List<RepairTicket.RepairStatus> statuses
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();
        User currentUser = userRepo.findByEmail(email);

        List<RepairTicket> tickets;

        if(currentUser.getRole() == User.UserRole.OPERATOR){
            tickets = ticketRepo.findByReportedBy_UserId(currentUser.getUserId());
        }
        else if (currentUser.getRole() == User.UserRole.MECHANIC){

            List<RepairTicket.RepairStatus> effectiveStatuses =
                    (statuses == null || statuses.isEmpty())
                            ? List.of(RepairTicket.RepairStatus.OPEN)
                            : statuses;

            tickets = ticketRepo.findByStatusIn(effectiveStatuses);
        }
        else {
            tickets = ticketRepo.findAll();
        }

        return tickets.stream()
                .map(mapper::toDto)
                .toList();
    }

    public MechanicRepairTicketResp mechanicTakesTicket(Long id){

        MechanicRepairTicketResp response;

        Optional<RepairTicket> ticket = ticketRepo.findById(id);

        if(ticket.isEmpty()){
            response = MechanicRepairTicketResp.builder()
                    .statusCode("404")
                    .message("Ticket not found")
                    .build();

            return response;
        }

        RepairTicket repairTicket = ticket.get();

        if(repairTicket.getStatus() != OPEN){
            return MechanicRepairTicketResp.builder()
                    .statusCode("409")
                    .message("Ticket is not available for assignment")
                    .build();
        }


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepo.findByEmail(username);

        repairTicket.setTechnician(user);
        repairTicket.setStatus(IN_PROGRESS);

        ticketRepo.save(repairTicket);

        response = MechanicRepairTicketResp.builder()
                .statusCode("200")
                .message("Ticket assigned successfully to mechanic")
                .repairRequestId(repairTicket.getRepairRequestId())
                .equipmentName(repairTicket.getEquipment().getName())
                .reportedBy(repairTicket.getReportedBy().getName())
                .technician(user.getName())
                .description(repairTicket.getDescription())
                .priority(repairTicket.getPriority())
                .status(repairTicket.getStatus())
                .build();

        return response;
    }

    public CompleteRepairTicketResp mechanicFinalizesTicket(CompleteRepairTicketReq request){

        CompleteRepairTicketResp response;

        Optional<RepairTicket> optionalRepairTicket = ticketRepo.findById(request.getTicketId());
        if(optionalRepairTicket.isEmpty()){
            response = CompleteRepairTicketResp.builder()
                    .message("Ticket doesn't exist")
                    .statusCode("404")
                    .build();

            return response;
        }
        RepairTicket repairTicket = optionalRepairTicket.get();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String technicianName = authentication.getName();
        System.out.println(technicianName);

        if (! technicianName.equals(repairTicket.getTechnician().getEmail())){
            response = CompleteRepairTicketResp.builder()
                    .message("Technician not assigned to ticket")
                    .statusCode("403")
                    .build();

            return response;
        }

        if (repairTicket.getStatus() != RepairTicket.RepairStatus.IN_PROGRESS) {
            response = CompleteRepairTicketResp.builder()
                    .statusCode("409")
                    .message("Only tickets in progress can be finalized")
                    .build();

            return response;
        }

        RepairTicket.RepairStatus ticketStatus = request.getStatus();
        BigDecimal cost = request.getCost();
        Instant resolvedAt = Instant.now();

        repairTicket.setStatus(ticketStatus);
        repairTicket.setCost(cost);
        repairTicket.setResolvedAt(resolvedAt);

        ticketRepo.save(repairTicket);

        response = CompleteRepairTicketResp.builder()
                .statusCode("200")
                .message("Ticket updated successfully")
                .repairTicketId(request.getTicketId())
                .equipmentName(repairTicket.getEquipment().getName())
                .reportedBy(repairTicket.getReportedBy().getName())
                .technician(repairTicket.getTechnician().getName())
                .description(repairTicket.getDescription())
                .priority(repairTicket.getPriority())
                .status(repairTicket.getStatus())
                .resolvedAt(repairTicket.getResolvedAt())
                .cost(repairTicket.getCost())
                .build();

        return response;
    }
}
