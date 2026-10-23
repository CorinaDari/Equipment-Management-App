package org.example.mapper;

import org.example.domainEntities.Equipment;
import org.example.dto.EquipmentResp;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")

public interface EquipmentMapper {

    @Mapping(source = "equipmentCategory.name", target = "categoryName")
    @Mapping(source = "location.name", target = "location")
    @Mapping(source= "equipmentId", target= "id")

    EquipmentResp toResp(Equipment equipment);

    @Mapping(source = "categoryName", target = "equipmentCategory.name")
    @Mapping(source = "location", target = "location.name")
    @Mapping(source= "id", target= "equipmentId")

    Equipment toEntity(EquipmentResp resp);
}
