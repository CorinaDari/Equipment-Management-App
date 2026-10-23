package org.example.mapper;

import org.example.domainEntities.RepairTicket;
import org.example.dto.GetAllRepairTickets;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RepairTicketMapper {
    @Mapping(source="equipment.name", target= "equipmentName")
    @Mapping(source="reportedBy.name", target= "reportedBy")
    @Mapping(source="technician.name", target= "technician")
    GetAllRepairTickets toDto(RepairTicket ticket);
}
