package com.test.ticketmanagement.controller;

import com.test.ticketmanagement.api.model.TicketRequest;
import com.test.ticketmanagement.api.model.TicketResponse;
import com.test.ticketmanagement.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TicketApiDelegateImplTest {

    @Mock
    private TicketService ticketService;

    @InjectMocks
    private TicketApiDelegateImpl ticketApiDelegate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTicket() {
        TicketRequest request = new TicketRequest();
        TicketResponse response = new TicketResponse();
        when(ticketService.createTicket(request)).thenReturn(response);

        ResponseEntity<TicketResponse> result = ticketApiDelegate.createTicket(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void testGetAllTickets() {
        List<TicketResponse> tickets = Arrays.asList(new TicketResponse(), new TicketResponse());
        when(ticketService.getAllTickets()).thenReturn(tickets);

        ResponseEntity<List<TicketResponse>> result = ticketApiDelegate.getAllTickets();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(tickets, result.getBody());
    }

    @Test
    void testGetTicketById() {
        Long id = 1L;
        TicketResponse response = new TicketResponse();
        when(ticketService.getTicketById(id)).thenReturn(response);

        ResponseEntity<TicketResponse> result = ticketApiDelegate.getTicketById(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void testUpdateTicket() {
        Long id = 1L;
        TicketRequest request = new TicketRequest();
        TicketResponse response = new TicketResponse();
        when(ticketService.updateTicket(id, request)).thenReturn(response);

        ResponseEntity<TicketResponse> result = ticketApiDelegate.updateTicket(id, request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void testDeleteTicket() {
        Long id = 1L;

        ResponseEntity<Void> result = ticketApiDelegate.deleteTicket(id);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(ticketService).deleteTicket(id);
    }

    @Test
    void testAssignTicket() {
        Long id = 1L;
        Long userId = 2L;
        TicketResponse response = new TicketResponse();
        when(ticketService.assignTicket(id, userId)).thenReturn(response);

        ResponseEntity<TicketResponse> result = ticketApiDelegate.assignTicket(id, userId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }
}
