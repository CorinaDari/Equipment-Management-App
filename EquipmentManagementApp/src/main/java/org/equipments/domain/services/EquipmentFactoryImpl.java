package org.equipments.domain.services;

import org.equipments.classes.Equipment;

public class EquipmentFactoryImpl implements EquipmentFactory {

    @Override
    public Equipment createEquipment(int id, String name, String type, String status, String location, double price) {
        if (id < 0) {
            throw new IllegalArgumentException("ID cannot be negative");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (status == null || status.isEmpty()) {
            throw new IllegalArgumentException("Status cannot be null or empty");
        }
        return new Equipment(id, name, type, status, location, price);
    }
}