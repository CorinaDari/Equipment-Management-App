package org.example.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.domainEntities.RepairTicket;

import java.math.BigDecimal;

@Getter
@Setter
public class CompleteRepairTicketReq {
    @NotNull
    private Long ticketId;

    @NotNull
    private RepairTicket.RepairStatus status;

    @NotNull @DecimalMin("0.00")
    private BigDecimal cost;
}
