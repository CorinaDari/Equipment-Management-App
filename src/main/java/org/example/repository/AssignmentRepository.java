package org.example.repository;

import org.example.domainEntities.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    @Query("""
        SELECT a
        FROM Assignment a
        WHERE a.user.userId = :userId
          AND a.equipment.equipmentId = :equipmentId
          AND a.startDate <= :now
          AND (a.endDate IS NULL OR a.endDate >= :now)
        """)

    Optional<Assignment> findActiveAsignment(
            @Param("userId") Long userId,
            @Param("equipmentId") Long eqipmentId,
            @Param("now") Instant now
    );
}
