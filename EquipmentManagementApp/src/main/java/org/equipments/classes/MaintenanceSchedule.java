package org.equipments.classes;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class MaintenanceSchedule {
    private List<EquipmentMaintenance> schedule = new ArrayList<>();

    public void addMaintenance(Equipment equipment, Maintenance maintenance) {
        EquipmentMaintenance equipmentMaintenance = findOrCreateEquipmentMaintenance(equipment);
        equipmentMaintenance.getMaintenances().add(maintenance);
    }

    public List<Maintenance> getScheduleForEquipment(Equipment equipment) {
        for (EquipmentMaintenance em : schedule) {
            if (em.getEquipment().equals(equipment)) {
                return em.getMaintenances();
            }
        }
        return new ArrayList<>();
    }

    private EquipmentMaintenance findOrCreateEquipmentMaintenance(Equipment equipment) {
        for (EquipmentMaintenance em : schedule) {
            if (em.getEquipment().equals(equipment)) {
                return em;
            }
        }
        EquipmentMaintenance newEntry = new EquipmentMaintenance(equipment, new ArrayList<>());
        schedule.add(newEntry);
        return newEntry;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EquipmentMaintenance {
        private Equipment equipment;
        private List<Maintenance> maintenances;
    }
}
