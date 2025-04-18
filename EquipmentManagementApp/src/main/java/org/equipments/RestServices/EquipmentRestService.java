package org.equipments.RestServices;

import org.equipments.classes.Equipment;
import org.equipments.domain.services.EquipmentService;
import org.equipments.dto.EquipmentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equipment")
public class EquipmentRestService {

    private final EquipmentService equipmentService;

    public EquipmentRestService(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @GetMapping
    public List<EquipmentDTO> getAllEquipment() {
        return equipmentService.getAllEquipment().stream()
                .map(this::convertToDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipmentDTO> getEquipmentById(@PathVariable int id) {
        Equipment equipment = equipmentService.getEquipmentById(id);
        if (equipment != null) {
            return ResponseEntity.ok(convertToDTO(equipment));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping
    public ResponseEntity<EquipmentDTO> addEquipment(@RequestBody EquipmentDTO equipmentDTO) {
        Equipment equipment = convertToEntity(equipmentDTO);
        Equipment savedEquipment = equipmentService.addEquipment(equipment);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(savedEquipment));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipmentDTO> updateEquipment(@PathVariable int id, @RequestBody EquipmentDTO equipmentDTO) {
        Equipment updatedEquipment = equipmentService.updateEquipment(id, convertToEntity(equipmentDTO));
        if (updatedEquipment != null) {
            return ResponseEntity.ok(convertToDTO(updatedEquipment));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipment(@PathVariable int id) {
        equipmentService.deleteEquipment(id);
        return ResponseEntity.noContent().build();
    }

    // Alte metode vor fi refactorizate similar...

    private EquipmentDTO convertToDTO(Equipment equipment) {
        return new EquipmentDTO(
                equipment.getEquipmentId(),
                equipment.getName(),
                equipment.getType(),
                equipment.getStatus(),
                equipment.getAcquisitionDate(),
                equipment.getPrice(),
                equipment.getLocation()
        );
    }

    private Equipment convertToEntity(EquipmentDTO equipmentDTO) {
        return new Equipment(
                equipmentDTO.getEquipmentId(),
                equipmentDTO.getName(),
                equipmentDTO.getType(),
                equipmentDTO.getStatus(),
                equipmentDTO.getLocation(),
                equipmentDTO.getPrice()
        );
    }
}
