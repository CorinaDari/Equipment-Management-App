package org.example.service;

import org.example.domainEntities.Equipment;
import org.example.domainEntities.EquipmentCategory;
import org.example.domainEntities.Location;
import org.example.domainEntities.RepairTicket;
import org.example.dto.CreateEquipmentReq;
import org.example.dto.CreateEquipmentResp;
import org.example.dto.EquipmentResp;
import org.example.dto.UpdateEquipmentReq;
import org.example.mapper.EquipmentMapper;
import org.example.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@Service
public class EquipmentService {

    private final EquipmentRepository equipRepo;
    private final EquipmentCategoryRepository categoryRepo;
    private final LocationRepository locationRepo;
    private final EquipmentMapper equipMapper;
    private final AssignmentRepository assignmentRepo;
    private final RepairTicketRepository repairTicketRepo;

    public EquipmentService(EquipmentRepository equipRepo,
                            EquipmentCategoryRepository categoryRepo,
                            LocationRepository locationRepo,
                            EquipmentMapper equipMapper,
                            AssignmentRepository assignmentRepo,
                            RepairTicketRepository repairTicketRepo){

        this.equipRepo = equipRepo;
        this.categoryRepo = categoryRepo;
        this.locationRepo = locationRepo;
        this.equipMapper = equipMapper;
        this.assignmentRepo = assignmentRepo;
        this.repairTicketRepo = repairTicketRepo;
    }

    public EquipmentResp getEquipmentById(Long id) {

        Equipment equipment = equipRepo.findByEquipmentIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Equipment not found"
                ));

        return equipMapper.toResp(equipment);
    }

    public List<EquipmentResp> getAllEquipment() {

        return equipRepo.findAllByDeletedAtIsNull()
                .stream()
                .map(equipMapper::toResp)
                .toList();
    }

    @Transactional
    public CreateEquipmentResp createEquipment(CreateEquipmentReq request){
        if(equipRepo.existsBySerialNumber(request.getSerialNumber())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Equipment with this serial number already exists");
        }

        var category = categoryRepo.findById(request.getEquipmentCategoryId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Equipment category not found"
                ));

        var location = locationRepo.findById(request.getLocationId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Location not found"
                ));

        Equipment equipment = equipMapper.toEntity(request);

        equipment.setEquipmentCategory(category);
        equipment.setLocation(location);

        Equipment savedEquipment = equipRepo.save(equipment);

        return equipMapper.toCreateEquipmentResp(savedEquipment);
    }

    @Transactional
    public EquipmentResp updateEquipment(Long id, UpdateEquipmentReq request) {

        Equipment equipment = equipRepo.findByEquipmentIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Equipment not found"
                ));

        if (request.getSerialNumber() != null &&
                equipRepo.existsBySerialNumberAndEquipmentIdNot(
                        request.getSerialNumber(), id)) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Serial number already exists"
            );
        }

        if (request.getEquipmentCategoryId() != null) {

            EquipmentCategory category = categoryRepo
                    .findById(request.getEquipmentCategoryId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Equipment category not found"
                    ));

            equipment.setEquipmentCategory(category);
        }

        if (request.getLocationId() != null) {

            Location location = locationRepo
                    .findById(request.getLocationId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Location not found"
                    ));

            equipment.setLocation(location);
        }

        equipMapper.updateEquipment(request, equipment);

        Equipment updatedEquipment = equipRepo.save(equipment);

        return equipMapper.toResp(updatedEquipment);
    }

    @Transactional
    public void deleteEquipment(Long id) {

        Equipment equipment = equipRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Equipment not found"
                ));

        if (equipment.getDeletedAt() != null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Equipment is already archived"
            );
        }

        boolean hasActiveAssignment =
                assignmentRepo.existsByEquipmentEquipmentIdAndEndDateIsNull(id);

        if (hasActiveAssignment) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Equipment has an active assignment"
            );
        }

        // Check active repair ticket
        boolean hasActiveRepair =
                repairTicketRepo.existsByEquipmentEquipmentIdAndStatusIn(
                        id,
                        List.of(
                                RepairTicket.RepairStatus.OPEN,
                                RepairTicket.RepairStatus.IN_PROGRESS
                        )
                );

        if (hasActiveRepair) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Equipment has an active repair ticket"
            );
        }

        equipment.setDeletedAt(Instant.now());

        equipRepo.save(equipment);
    }
}
