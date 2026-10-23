package org.example.repository;

import org.example.domainEntities.RepairTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface RepairTicketRepository extends JpaRepository<RepairTicket, Long> {
    List<RepairTicket> findByReportedBy_UserId(Long userId);

    boolean existsByEquipment_EquipmentIdAndStatusIn(
            Long equipmentId,
            Collection<RepairTicket.RepairStatus> statuses
    );


}
