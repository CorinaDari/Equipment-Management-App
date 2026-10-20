package org.example.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.example.domainEntities.EquipmentCategory;
import org.example.dto.EquipmentCategoryDto;
import org.example.mapper.EquipmentCategoryMapper;
import org.example.repository.EquipmentCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/equipCategory")
public class EquipmentCategoryController {

    @Autowired
    EquipmentCategoryRepository equipCategoryRep;

    @Autowired
    EquipmentCategoryMapper mapper;

    @GetMapping("/{id}")
    public ResponseEntity<EquipmentCategoryDto> getEquipmentCategory(@PathVariable Long id){

        var equipmentCategory = equipCategoryRep.findById(id).orElse(null);

        if(equipmentCategory == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(mapper.toDto(equipmentCategory));
    }

    @GetMapping
    public List<EquipmentCategoryDto> getAllCategories(){
        List<EquipmentCategoryDto> list = new ArrayList<>();

        List<EquipmentCategory> allEquipments = equipCategoryRep.findAll();

        for (EquipmentCategory allEquipment : allEquipments) {
            list.add(mapper.toDto(allEquipment));
        }

        return list;
    }

    @PostMapping
    public List<EquipmentCategoryDto> createEquipmentCategory(@RequestBody List<EquipmentCategoryDto> dto){

        List<EquipmentCategoryDto> list = new ArrayList<>();

        for (EquipmentCategoryDto cat : dto){
            EquipmentCategory entity = mapper.toEntity(cat);
            equipCategoryRep.save(entity);

            list.add(mapper.toDto(entity));
        }

        return list;

    }
    @GetMapping("/csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest request){
        return (CsrfToken) request.getAttribute("_csrf");
    }
}
