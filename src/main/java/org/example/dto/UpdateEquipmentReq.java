package org.example.dto;

import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter
public class UpdateEquipmentReq {

    private Long equipmentCategoryId;

    private Long locationId;

    private String name;

    private String serialNumber;

    private String manufacturer;

    private String model;

    private LocalDate acquisitionDate;

    @PositiveOrZero
    private BigDecimal price;
}