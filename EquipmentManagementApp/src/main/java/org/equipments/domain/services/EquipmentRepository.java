package org.equipments.domain.services;

import org.equipments.classes.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Integer> {

    List<Equipment> findByStatus(String status);
    List<Equipment> findByType(String type);

    List<Equipment> findByLocation(String location);
}