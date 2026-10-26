package org.example.service;

import jakarta.transaction.Transactional;
import org.example.domainEntities.Assignment;
import org.example.domainEntities.Equipment;
import org.example.domainEntities.User;
import org.example.dto.AssignEquipmentReq;
import org.example.dto.AssignEquipmentResp;
import org.example.repository.AssignmentRepository;
import org.example.repository.EquipmentRepository;
import org.example.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AssignEquipmentService {

    private final AssignmentRepository assignRepo;
    private final UserRepository userRepo;
    private final EquipmentRepository equipRepo;

    public AssignEquipmentService(AssignmentRepository assignRepo,UserRepository userRepo, EquipmentRepository equipRepo){
        this.assignRepo = assignRepo;
        this.userRepo = userRepo;
        this.equipRepo = equipRepo;
    }

    @Transactional
    public AssignEquipmentResp assignEquipment(AssignEquipmentReq request){

        if(request.getStartDate().isAfter(request.getEndDate())){
            return AssignEquipmentResp.builder()
                    .statusCode("400")
                    .message("Start date cannot be grater than end date")
                    .build();
        }

        if(request.getStartDate().equals(request.getEndDate())){
            return AssignEquipmentResp.builder()
                    .statusCode("400")
                    .message("Start date cannot be equal with end date")
                    .build();
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User authenticatedUser = userRepo.findByEmail(username);

        if(authenticatedUser.getRole() != User.UserRole.MANAGER){
            return AssignEquipmentResp.builder()
                    .statusCode("403")
                    .message("Not allowed to assign")
                    .build();
        }

        Optional<Equipment> optionalEquipment = equipRepo.findById(request.getEquipmentId());
        if(optionalEquipment.isEmpty()){
            return AssignEquipmentResp.builder()
                    .statusCode("404")
                    .message("Equipment not found")
                    .build();
        }

        Equipment equipmentEntity = optionalEquipment.get();

        if(equipmentEntity.getStatus() != Equipment.EquipmentStatus.AVAILABLE){
            return AssignEquipmentResp.builder()
                    .statusCode("409")
                    .message("Equipment not currently available for assignation")
                    .build();
        }

        Optional<User> optionalWorker = userRepo.findById(request.getUserId());
        if(optionalWorker.isEmpty()){
            return AssignEquipmentResp.builder()
                    .statusCode("404")
                    .message("Worker not found")
                    .build();
        }

        User worker = optionalWorker.get();
        if(!worker.isActive()){
            return AssignEquipmentResp.builder()
                    .statusCode("404")
                    .message("Worker not available for assignment")
                    .build();
        }

        Assignment assignment = Assignment.builder()
                .equipment(equipmentEntity)
                .user(worker)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .notes(request.getNotes())
                .build();

        assignRepo.save(assignment);

        equipmentEntity.setStatus(Equipment.EquipmentStatus.ASSIGNED);
        equipRepo.save(equipmentEntity);

        return AssignEquipmentResp.builder()
                .statusCode("201")
                .message("Assignment created")
                .assignmentId(assignment.getAssignmentId())
                .equipmentId(equipmentEntity.getEquipmentId())
                .equipmentName(equipmentEntity.getName())
                .userId(worker.getUserId())
                .workerName(worker.getName())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();
    }
}
