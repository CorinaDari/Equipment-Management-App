package org.example.domainEntities;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "repair_request")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RepairTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "repair_request_id")
    private Long repairRequestId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reported_by", nullable = false)
    private User reportedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "technician_id")
    private User technician;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;
    public enum RepairPriority { LOW, MEDIUM, HIGH, CRITICAL }
    public enum RepairStatus { OPEN, IN_PROGRESS, RESOLVED, CLOSED }

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RepairPriority priority = RepairPriority.MEDIUM;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RepairStatus status = RepairStatus.OPEN;

    @CreationTimestamp
    @Column(name = "reported_at", nullable = false, updatable = false)
    private Instant reportedAt;

    @Column(name = "resolved_at")
    private Instant resolvedAt;

    @Column(precision = 10, scale = 2)
    private BigDecimal cost;
}