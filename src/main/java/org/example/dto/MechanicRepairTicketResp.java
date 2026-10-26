package org.example.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.domainEntities.RepairTicket;

@Setter
@Getter
@Builder
public class MechanicRepairTicketResp {
    private String statusCode;
    private String message;
    private Long repairRequestId;
    private String equipmentName;
    private String reportedBy;
    private String technician;
    private String description;
    private RepairTicket.RepairPriority priority;
    private RepairTicket.RepairStatus status;

}
