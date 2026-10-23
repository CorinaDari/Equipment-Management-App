package org.example.dto;


import lombok.Getter;
import lombok.Setter;
import org.example.domainEntities.RepairTicket;

import java.math.BigDecimal;
import java.time.Instant;

@Getter @Setter
public class GetAllRepairTickets {

    private Long repairRequestId;
    private String equipmentName;
    private String reportedBy;
    private String technician;
    private String description;
    private RepairTicket.RepairPriority priority;
    private RepairTicket.RepairStatus status;
    private Instant reportedAt;
    private Instant resolvedAt;
    private BigDecimal cost;
}
