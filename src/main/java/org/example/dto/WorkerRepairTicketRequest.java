package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.domainEntities.RepairTicket;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WorkerRepairTicketRequest {
    private Long id;
    private String description;
    private RepairTicket.RepairPriority priority;
}
