package org.example.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter @Setter @Builder
public class AssignEquipmentResp {
    private String statusCode;
    private String message;
    private Long assignmentId;
    private Long equipmentId;
    private String equipmentName;
    private Long userId;
    private String workerName;
    private Instant startDate;
    private Instant endDate;
    private String notes;
}
