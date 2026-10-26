package org.example.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter @Setter @Builder
public class AssignEquipmentReq {
    @NotNull
    private Long userId;
    @NotNull
    private Long equipmentId;
    @NotNull
    private Instant startDate;
    @NotNull
    private Instant endDate;
    private String notes;
}
