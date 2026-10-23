package org.example.controllers;

import org.example.dto.GetAllRepairTickets;
import org.example.dto.WorkerRepairTicketRequest;
import org.example.dto.WorkerRepairTicketResponse;
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
    public List<GetAllRepairTickets> getAllRepairTickets(){
        return service.getRepairTickets();
    }
}
