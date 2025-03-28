package org.equipments.classes;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Inventory {
    private List<Equipment> equipments = new ArrayList<>();
    private List<Integer> quantities = new ArrayList<>();


    public void addOrUpdateEquipment(Equipment equipment, int quantity) {
        int index = equipments.indexOf(equipment);
        if (index != -1) {

            quantities.set(index, quantities.get(index) + quantity);
        } else {

            equipments.add(equipment);
            quantities.add(quantity);
        }
    }


    public boolean removeEquipment(Equipment equipment, int quantity) {
        int index = equipments.indexOf(equipment);
        if (index != -1 && quantities.get(index) >= quantity) {
            quantities.set(index, quantities.get(index) - quantity);
            return true;
        }
        return false;
    }


    public int getStockLevel(Equipment equipment) {
        int index = equipments.indexOf(equipment);
        return (index != -1) ? quantities.get(index) : 0;
    }


    public void setInventory(List<Equipment> equipments, List<Integer> quantities) {
        if (equipments.size() != quantities.size()) {
            throw new IllegalArgumentException("Equipment and quantities lists must have the same size.");
        }
        this.equipments = equipments;
        this.quantities = quantities;
    }
}
