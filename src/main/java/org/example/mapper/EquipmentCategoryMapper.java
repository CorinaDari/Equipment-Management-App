package org.example.mapper;

import org.example.domainEntities.EquipmentCategory;
import org.example.dto.EquipmentCategoryDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EquipmentCategoryMapper {
    EquipmentCategoryDto toDto(EquipmentCategory equipCategory);

    EquipmentCategory toEntity(EquipmentCategoryDto dto);
}
