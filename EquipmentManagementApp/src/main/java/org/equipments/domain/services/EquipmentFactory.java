package org.equipments.domain.services;

import org.equipments.classes.Equipment;

public interface EquipmentFactory {
    Equipment createEquipment(int id, String name, String type, String status, String location, double price);
}