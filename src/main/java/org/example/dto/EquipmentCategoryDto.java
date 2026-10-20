package org.example.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EquipmentCategoryDto {

    private Long equipmentCategoryId;
    private String name;
    private String description;

}
