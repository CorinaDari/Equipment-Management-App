package org.example.controllers;

import jakarta.validation.Valid;
import org.example.dto.CreateEquipmentReq;
import org.example.dto.CreateEquipmentResp;
import org.example.dto.EquipmentResp;
import org.example.dto.UpdateEquipmentReq;
import org.example.service.EquipmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equipments")
public class EquipmentController {
    private EquipmentService service;

    public EquipmentController(EquipmentService service){
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipmentResp> getEquipmentById(@PathVariable Long id){

        EquipmentResp resp = service.getEquipmentById(id);

        return ResponseEntity.ok(resp);
    }

    @GetMapping
    public ResponseEntity<List<EquipmentResp>> getAllEquipment() {

        List<EquipmentResp> response = service.getAllEquipment();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<CreateEquipmentResp> createEquipment(
            @Valid @RequestBody CreateEquipmentReq request){

        CreateEquipmentResp response = service.createEquipment(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);

    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<EquipmentResp> updateEquipment(
            @PathVariable Long id,
            @Valid @RequestBody UpdateEquipmentReq request) {

        EquipmentResp response = service.updateEquipment(id, request);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/archive")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Void> deleteEquipment(@PathVariable Long id) {

        service.deleteEquipment(id);

        return ResponseEntity.noContent().build();
    }
}
