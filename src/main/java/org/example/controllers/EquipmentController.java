package org.example.controllers;

import org.example.domainEntities.Equipment;
import org.example.mapper.EquipmentMapper;
import org.example.dto.EquipmentResp;
import org.example.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/equipments")
public class EquipmentController {
    @Autowired
    EquipmentRepository equipmentRepository;

    @Autowired
    EquipmentMapper mapper;

    @GetMapping("/{id}")
    public ResponseEntity<EquipmentResp> getEquipmentById(@PathVariable Long id){
        var equipment = equipmentRepository.findById(id).orElse(null);

        if(equipment == null){
            return ResponseEntity.notFound().build();
        }
        EquipmentResp resp = mapper.toResp(equipment);

        return ResponseEntity.ok(resp);
    }

    @GetMapping("/")
    public List<EquipmentResp> getAllEquipmentById(){

        List<EquipmentResp> equipDtoList = new ArrayList<>();
        List<Equipment> equipList = equipmentRepository.findAll();

        for(Equipment equipment: equipList){
            equipDtoList.add(mapper.toResp(equipment));
        }

        return equipDtoList;


    }


}
