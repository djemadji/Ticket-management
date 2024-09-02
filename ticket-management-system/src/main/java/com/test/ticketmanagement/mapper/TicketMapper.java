package com.test.ticketmanagement.mapper;

import com.test.ticketmanagement.api.model.TicketRequest;
import com.test.ticketmanagement.api.model.TicketResponse;
import com.test.ticketmanagement.model.Ticket;
import com.test.ticketmanagement.model.TicketStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", imports = TicketStatus.class)
public interface TicketMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "assignedUser", ignore = true)
    @Mapping(target = "status", expression = "java(TicketStatus.valueOf(ticketRequest.getStatus().name()))")
    Ticket toEntity(TicketRequest ticketRequest);

    TicketResponse toResponse(Ticket ticket);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "assignedUser", ignore = true)
    @Mapping(target = "status", expression = "java(TicketStatus.valueOf(ticketRequest.getStatus().name()))")
    void updateTicketFromRequest(TicketRequest ticketRequest, @MappingTarget Ticket ticket);

}