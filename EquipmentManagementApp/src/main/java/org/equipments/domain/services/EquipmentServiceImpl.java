package org.equipments.domain.services;

import org.equipments.classes.Equipment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipmentServiceImpl implements EquipmentService {

    private final List<Equipment> equipmentList = new ArrayList<>();

    @Override
    public List<Equipment> getAllEquipment() {
        return new ArrayList<>(equipmentList);
    }

    @Override
    public Equipment getEquipmentById(int id) {
        return equipmentList.stream()
                .filter(equipment -> equipment.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Equipment addEquipment(Equipment equipment) {
        equipmentList.add(equipment);
        return equipment;
    }

    @Override
    public Equipment updateEquipment(int id, Equipment updatedEquipment) {
        for (int i = 0; i < equipmentList.size(); i++) {
            Equipment existingEquipment = equipmentList.get(i);
            if (existingEquipment.getId() == id) {
                equipmentList.set(i, updatedEquipment);
                return updatedEquipment;
            }
        }
        return null;
    }

    @Override
    public void deleteEquipment(int id) {
        equipmentList.removeIf(equipment -> equipment.getId() == id);
    }

    @Override
    public List<Equipment> getEquipmentByStatus(String status) {
        return equipmentList.stream()
                .filter(equipment -> equipment.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }

    @Override
    public List<Equipment> getEquipmentByLocation(String location) {
        return equipmentList.stream()
                .filter(equipment -> equipment.getLocation().equalsIgnoreCase(location))
                .collect(Collectors.toList());
    }

    // Metode noi

    @Override
    public double calculateTotalValue() {
        return equipmentList.stream()
                .mapToDouble(Equipment::getPrice)
                .sum();
    }

    @Override
    public Equipment findMostExpensiveEquipment() {
        return equipmentList.stream()
                .max(Comparator.comparingDouble(Equipment::getPrice))
                .orElse(null); // Returnăm `null` dacă lista e goală
    }

    @Override
    public boolean isEquipmentAvailable(int id) {
        Equipment equipment = getEquipmentById(id);
        return equipment != null && "Available".equalsIgnoreCase(equipment.getStatus());
    }

    @Override
    public List<Equipment> filterByPriceRange(double minPrice, double maxPrice) {
        return null;
    }

    @Override
    public Equipment saveEquipment(Equipment equipment) {
        return null;
    }
}
