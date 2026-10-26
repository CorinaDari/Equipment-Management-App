package org.example.controllers;

import jakarta.validation.Valid;
import org.example.domainEntities.RepairTicket;
import org.example.dto.*;
import org.example.service.RepairTicketService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RepairTicketController {

    private final RepairTicketService service;

    public RepairTicketController(RepairTicketService service) {
        this.service = service;
    }

    @PostMapping("/repairTicket")
    public WorkerRepairTicketResponse sendRepairRequest(@RequestBody WorkerRepairTicketRequest requestBody){
        return service.createRepairTicket(requestBody);
    }

    @GetMapping("/repairTicket")
    public List<GetAllRepairTickets> getAllRepairTickets(
            @RequestParam(required = false)
            List<RepairTicket.RepairStatus> status
    ){
        return service.getRepairTickets(status);
    }

    @PatchMapping("/repairTicket/{id}")
    public MechanicRepairTicketResp mechanicTakesTicket(
            @PathVariable Long id
    ){
        return service.mechanicTakesTicket(id);
    }

    @PatchMapping("/repairTicket/finalize")
    public CompleteRepairTicketResp mechanicFinalizesTicket(@Valid @RequestBody CompleteRepairTicketReq request){
        return service.mechanicFinalizesTicket(request);
    }
}
