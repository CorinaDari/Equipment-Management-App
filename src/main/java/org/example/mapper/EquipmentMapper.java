package org.example.mapper;

import org.example.domainEntities.Equipment;
import org.example.dto.CreateEquipmentReq;
import org.example.dto.CreateEquipmentResp;
import org.example.dto.EquipmentResp;
import org.example.dto.UpdateEquipmentReq;
import org.mapstruct.*;

@Mapper(componentModel = "spring")

public interface EquipmentMapper {
    @Mapping(source = "equipmentCategory.name", target = "categoryName")
    @Mapping(source = "location.name", target = "location")
    @Mapping(source = "equipmentId", target = "id")
    EquipmentResp toResp(Equipment equipment);

    @Mapping(target = "equipmentId", source = "equipmentId")
    @Mapping(target = "message", constant = "Equipment created successfully")
    CreateEquipmentResp toCreateEquipmentResp(Equipment equipment);

    @Mapping(target = "equipmentId", ignore = true)
    @Mapping(target = "equipmentCategory", ignore = true)
    @Mapping(target = "location", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "engineHours", ignore = true)

    Equipment toEntity(CreateEquipmentReq request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "equipmentId", ignore = true)
    @Mapping(target = "equipmentCategory", ignore = true)
    @Mapping(target = "location", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "engineHours", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)

    void updateEquipment(
            UpdateEquipmentReq request,
            @MappingTarget Equipment equipment
    );

}
