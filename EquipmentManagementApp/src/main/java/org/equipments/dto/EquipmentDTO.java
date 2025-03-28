package org.equipments.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentDTO {

    @Getter
    private int equipmentId;
    private String name;
    private String type;
    private String status;
    private Date acquisitionDate;
    private double price;
    private String location;


    public EquipmentDTO(String updatedEquipment, double v, String inUse, String onSite) {
    }
}
