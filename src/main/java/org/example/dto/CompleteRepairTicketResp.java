package org.example.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.domainEntities.RepairTicket;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Builder
public class CompleteRepairTicketResp {
    private String statusCode;
    private String message;
    private Long repairTicketId;
    private String equipmentName;
    private String reportedBy;
    private String technician;
    private String description;
    private RepairTicket.RepairPriority priority;
    private RepairTicket.RepairStatus status;
    private Instant resolvedAt;
    private BigDecimal cost;
}
