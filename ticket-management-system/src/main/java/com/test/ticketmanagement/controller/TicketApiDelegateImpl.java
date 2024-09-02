package com.test.ticketmanagement.controller;

import com.test.ticketmanagement.api.TicketApiDelegate;
import com.test.ticketmanagement.api.model.TicketRequest;
import com.test.ticketmanagement.api.model.TicketResponse;
import com.test.ticketmanagement.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TicketApiDelegateImpl implements TicketApiDelegate {

    private final TicketService ticketService;

    @Override
    public ResponseEntity<TicketResponse> createTicket(TicketRequest ticketRequest) {
        TicketResponse ticketResponse = ticketService.createTicket(ticketRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketResponse);
    }

    @Override
    public ResponseEntity<List<TicketResponse>> getAllTickets() {
        List<TicketResponse> tickets = ticketService.getAllTickets();
        return ResponseEntity.ok(tickets);
    }

    @Override
    public ResponseEntity<TicketResponse> getTicketById(Long id) {
        TicketResponse ticket = ticketService.getTicketById(id);
        return ResponseEntity.ok(ticket);
    }

    @Override
    public ResponseEntity<TicketResponse> updateTicket(Long id, TicketRequest ticketRequest) {
        TicketResponse updatedTicket = ticketService.updateTicket(id, ticketRequest);
        return ResponseEntity.ok(updatedTicket);
    }

    @Override
    public ResponseEntity<Void> deleteTicket(Long id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<TicketResponse> assignTicket(Long id, Long userId) {
        TicketResponse assignedTicket = ticketService.assignTicket(id, userId);
        return ResponseEntity.ok(assignedTicket);
    }
}