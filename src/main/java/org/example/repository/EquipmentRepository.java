package org.example.repository;

import org.example.domainEntities.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    boolean existsBySerialNumber(String serialNumber);

    boolean existsBySerialNumberAndEquipmentIdNot(
            String serialNumber,
            Long equipmentId
    );

    List<Equipment> findAllByDeletedAtIsNull();
    Optional<Equipment> findByEquipmentIdAndDeletedAtIsNull(Long equipmentId);
}
