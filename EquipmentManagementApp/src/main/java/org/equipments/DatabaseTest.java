package org.equipments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.equipments.classes.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class DatabaseTest implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {

        List<Equipment> equipmentList = new ArrayList<>();


        Equipment equipment1 = Equipment.builder()
                .equipmentId(1)
                .name("Excavator")
                .type("Heavy Machinery")
                .status("Available")
                .acquisitionDate(new Date())
                .price(50000.0)
                .location("Warehouse A")
                .maintenanceHistory(new ArrayList<>())
                .repairHistory(new ArrayList<>())
                .build();

        Equipment equipment2 = Equipment.builder()
                .equipmentId(2)
                .name("Bulldozer")
                .type("Heavy Machinery")
                .status("In Use")
                .acquisitionDate(new Date())
                .price(75000.0)
                .location("Warehouse B")
                .maintenanceHistory(new ArrayList<>())
                .repairHistory(new ArrayList<>())
                .build();


        equipmentList.add(equipment1);
        equipmentList.add(equipment2);


        System.out.println("Lista de echipamente:");
        for (Equipment equipment : equipmentList) {
            System.out.println(equipment);
        }


        Acquisition acquisition = Acquisition.builder()
                .acquisitionId(1)
                .acquisitionDate(new Date())
                .totalAmount(125000.0)
                .equipmentList(equipmentList)
                .build();

        System.out.println("\nAchiziția creată:");
        System.out.println(acquisition);

//        EquipmentCatalog catalog = new EquipmentCatalog();
        EquipmentCatalog catalog = EquipmentCatalog.builder()
                .equipmentCatalogId(1)
                .build();
        catalog.addEquipment(equipment1);
        catalog.addEquipment(equipment2);

        System.out.println("\nEchipamente disponibile în catalog:");
        List<Equipment> availableEquipment = catalog.getAvailableEquipment();
        for (Equipment eq : availableEquipment) {
            System.out.println(eq);
        }

        
        RepairRequest repairRequest = RepairRequest.builder()
                .requestId(1)
                .equipment(equipment2)
                .issueDescription("Hydraulic system failure")
                .requestDate(new Date())
                .status("Pending")
                .repairActivities(new ArrayList<>(List.of("Check hydraulic pump", "Replace hoses")))
                .build();

        System.out.println("\nCererea de reparație:");
        System.out.println(repairRequest);
    }
}
