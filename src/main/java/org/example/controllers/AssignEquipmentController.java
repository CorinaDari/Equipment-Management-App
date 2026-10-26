package org.example.controllers;

import jakarta.validation.Valid;
import org.example.dto.AssignEquipmentReq;
import org.example.dto.AssignEquipmentResp;
import org.example.service.AssignEquipmentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AssignEquipmentController {

    private AssignEquipmentService service;

    public AssignEquipmentController(AssignEquipmentService service){
        this.service = service;
    }

    @PostMapping("/assignEquipment")
    public AssignEquipmentResp assignEquipment(@Valid @RequestBody AssignEquipmentReq request){
        return service.assignEquipment(request);
    }
}
