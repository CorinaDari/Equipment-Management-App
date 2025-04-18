package org.equipments.domain.services;

import org.equipments.classes.Equipment;
import org.springframework.stereotype.Service;

import java.util.List;

public interface EquipmentService {
    List<Equipment> getAllEquipment();
    Equipment getEquipmentById(int id);
    Equipment addEquipment(Equipment equipment);
    Equipment updateEquipment(int id, Equipment equipment);
    void deleteEquipment(int id);

    List<Equipment> getEquipmentByStatus(String status);
    List<Equipment> getEquipmentByLocation(String location);

    double calculateTotalValue();
    Equipment findMostExpensiveEquipment();
    boolean isEquipmentAvailable(int id);
    List<Equipment> filterByPriceRange(double minPrice, double maxPrice);

    Equipment saveEquipment(Equipment equipment);
}