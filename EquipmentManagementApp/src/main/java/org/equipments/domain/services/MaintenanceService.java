package org.equipments.domain.services;


import org.equipments.classes.Maintenance;
import java.util.List;

public interface MaintenanceService {
    List<Maintenance> getMaintenancesByEquipmentId(int equipmentId);
    Maintenance addMaintenance(Maintenance maintenance);
    Maintenance updateMaintenance(int maintenanceId, Maintenance maintenance);
    void deleteMaintenance(int maintenanceId);
}
