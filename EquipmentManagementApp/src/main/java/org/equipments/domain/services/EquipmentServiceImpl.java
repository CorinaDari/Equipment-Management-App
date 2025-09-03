package org.equipments.domain.services;

import jakarta.transaction.Transactional;
import org.equipments.classes.Equipment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipmentServiceImpl implements EquipmentService {

    private final EquipmentRepository equipmentRepository;

    public EquipmentServiceImpl(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    @Override
    @Transactional
    public List<Equipment> getAllEquipment() {
        return equipmentRepository.findAll();

    }

    @Override
    public Equipment getEquipmentById(int id) {
        return equipmentRepository.findById(id).orElse(null); 

    }

    @Override
    public Equipment addEquipment(Equipment equipment) {
        return equipmentRepository.save(equipment); 
    }

    @Override
    public Equipment updateEquipment(int id, Equipment equipment) {
        if (equipmentRepository.existsById(id)) {
            equipment.setEquipmentId(id); 
            return equipmentRepository.save(equipment);
        }
        return null;
    }

    @Override
    public void deleteEquipment(int id) {
            equipmentRepository.deleteById(id); 
    }

    @Override
    public Equipment saveEquipment(Equipment equipment) {
        return equipmentRepository.save(equipment);
    }

    @Override
    public List<Equipment> getEquipmentByStatus(String status) {
        return equipmentRepository.findByStatus(status);
    }

    @Override
    public List<Equipment> getEquipmentByLocation(String location) {
        return equipmentRepository.findByLocation(location);
    }

    @Override
    public double calculateTotalValue() {
        return equipmentRepository.findAll().stream()
                .mapToDouble(Equipment::getPrice)
                .sum();
    }

    @Override
    public Equipment findMostExpensiveEquipment() {
        return equipmentRepository.findAll().stream()
                .max((e1, e2) -> Double.compare(e1.getPrice(), e2.getPrice()))
                .orElse(null);
    }

    @Override
    public boolean isEquipmentAvailable(int id) {
        Equipment equipment = getEquipmentById(id);
        return equipment != null && "Available".equalsIgnoreCase(equipment.getStatus());
    }

    @Override
    public List<Equipment> filterByPriceRange(double minPrice, double maxPrice) {
        return equipmentRepository.findAll().stream()
                .filter(equipment -> equipment.getPrice() >= minPrice && equipment.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

}

