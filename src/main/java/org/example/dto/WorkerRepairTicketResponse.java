package org.example.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter @Getter
public class WorkerRepairTicketResponse {
    private String statusCode;
    private String message;
    private Long repairTicketId;
    private Instant createdAt;
    private String ticketState;

}
