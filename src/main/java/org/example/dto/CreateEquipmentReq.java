package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.domainEntities.Equipment;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter
public class CreateEquipmentReq {

    @NotNull
    private Long equipmentCategoryId;
    @NotNull
    private Long locationId;
    @NotBlank
    private String name;
    @NotBlank
    private String serialNumber;
    @NotBlank
    private String manufacturer;
    @NotBlank
    private String model;
    @NotNull
    private Equipment.EquipmentStatus status = Equipment.EquipmentStatus.AVAILABLE;
    @NotNull
    private LocalDate acquisitionDate;
    @NotNull
    private BigDecimal price;
}
