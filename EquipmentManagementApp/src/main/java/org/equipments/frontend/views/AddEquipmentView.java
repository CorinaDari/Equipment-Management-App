package org.equipments.frontend.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;


@Route("/addEquipment")
public class AddEquipmentView extends VerticalLayout{
    public AddEquipmentView() {
        add(new H1("Add Equipment"));
    }
//    private final EquipmentService equipmentService;
//    private final Grid<Equipment> equipmentGrid = new Grid<>(Equipment.class);
//
//    public AddEquipment(EquipmentService equipmentService) {
//        add(new H1("Formular echipamente"));
//        System.out.println("MainView loaded ✅");
//
//        this.equipmentService = equipmentService;
//
//        TextField name = new TextField("Name");
//        TextField type = new TextField("Type");
//        TextField status = new TextField("Status");
//        TextField location = new TextField("Location");
//        NumberField price = new NumberField("Price");
//        DatePicker acquisitionDate = new DatePicker("Acquisition Date");
//
//        Button addButton = new Button("Add Equipment", event -> {
//            Equipment equipment = Equipment.builder()
//                    .name(name.getValue())
//                    .type(type.getValue())
//                    .status(status.getValue())
//                    .location(location.getValue())
//                    .price(price.getValue())
//                    .acquisitionDate(convertToDate(acquisitionDate.getValue()))
//                    .build();
//
//            equipmentService.addEquipment(equipment);
//            updateGrid();
//        });
//
//        // Inițializare coloană manuală (evităm LazyInitializationException)
//        equipmentGrid.removeAllColumns();
//        equipmentGrid.addColumn(Equipment::getName).setHeader("Name");
//        equipmentGrid.addColumn(Equipment::getType).setHeader("Type");
//        equipmentGrid.addColumn(Equipment::getStatus).setHeader("Status");
//        equipmentGrid.addColumn(Equipment::getLocation).setHeader("Location");
//        equipmentGrid.addColumn(Equipment::getPrice).setHeader("Price");
//        equipmentGrid.addColumn(Equipment::getAcquisitionDate).setHeader("Acquisition Date");
//
//        // 🔥 Adăugăm coloana cu nr. mentenanțe (fără Lazy crash)
//        equipmentGrid.addColumn(e -> {
//            if (e.getMaintenanceHistory() == null) return 0;
//            return e.getMaintenanceHistory().size();
//        }).setHeader("Nr. mentenanțe");
//
//        add(name, type, status, location, price, acquisitionDate, addButton, equipmentGrid);
//        updateGrid();
//    }
//
//    private void updateGrid() {
//        equipmentGrid.setItems(equipmentService.getAllEquipment());
//    }
//
//    private Date convertToDate(LocalDate localDate) {
//        if (localDate == null) return null;
//        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
//    }
}
