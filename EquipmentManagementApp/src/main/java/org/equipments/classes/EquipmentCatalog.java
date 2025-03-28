package org.equipments.classes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
@Entity
public class EquipmentCatalog {
    @Id @GeneratedValue
    private int equipmentCatalogId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "catalog_id")
    private List<Equipment> equipmentList;

    public void addEquipment(Equipment equipment) {
        if (equipmentList == null) {
            equipmentList = new ArrayList<>();
        }
        equipmentList.add(equipment);
    }



    public List<Equipment> searchByType(String type) {
        List<Equipment> result = new ArrayList<>();
        for (Equipment equipment : equipmentList) {
            if (equipment.getType().equals(type)) {
                result.add(equipment);
            }
        }
        return result;
    }


    public List<Equipment> getAvailableEquipment() {
        List<Equipment> result = new ArrayList<>();
        for (Equipment equipment : equipmentList) {
            if ("Available".equals(equipment.getStatus())) {
                result.add(equipment);
            }
        }
        return result;
    }


}
