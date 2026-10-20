package org.example.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
public class EquipmentResp {
    private Long id;
    private String name;
    private String categoryName;
    private String location;
    private String model;
    private String status;
    private BigDecimal engineHours;

}
