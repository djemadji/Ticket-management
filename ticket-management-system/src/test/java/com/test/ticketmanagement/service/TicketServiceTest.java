package com.test.ticketmanagement.service;

import com.test.ticketmanagement.api.model.TicketRequest;
import com.test.ticketmanagement.api.model.TicketResponse;
import com.test.ticketmanagement.exception.ResourceNotFoundException;
import com.test.ticketmanagement.mapper.TicketMapper;
import com.test.ticketmanagement.model.Ticket;
import com.test.ticketmanagement.model.User;
import com.test.ticketmanagement.repository.TicketRepository;
import com.test.ticketmanagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TicketMapper ticketMapper;

    @InjectMocks
    private TicketService ticketService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTicket() {
        TicketRequest ticketRequest = new TicketRequest();
        ticketRequest.setTitle("Test Ticket");
        ticketRequest.setDescription("This is a test ticket");
        ticketRequest.setAssignedUserId(1L);

        User user = new User();
        user.setId(1L);

        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setTitle("Test Ticket");
        ticket.setDescription("This is a test ticket");
        ticket.setAssignedUser(user);

        TicketResponse ticketResponse = new TicketResponse();
        ticketResponse.setId(1L);
        ticketResponse.setTitle("Test Ticket");
        ticketResponse.setDescription("This is a test ticket");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(ticketMapper.toEntity(any(TicketRequest.class))).thenReturn(ticket);
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        when(ticketMapper.toResponse(any(Ticket.class))).thenReturn(ticketResponse);

        TicketResponse createdTicket = ticketService.createTicket(ticketRequest);

        assertNotNull(createdTicket);
        assertEquals(1L, createdTicket.getId());
        assertEquals("Test Ticket", createdTicket.getTitle());
        assertEquals("This is a test ticket", createdTicket.getDescription());
        verify(ticketRepository, times(1)).save(any(Ticket.class));
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTicketById() {
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setTitle("Test Ticket");

        TicketResponse ticketResponse = new TicketResponse();
        ticketResponse.setId(1L);
        ticketResponse.setTitle("Test Ticket");

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketMapper.toResponse(ticket)).thenReturn(ticketResponse);

        TicketResponse foundTicket = ticketService.getTicketById(1L);

        assertNotNull(foundTicket);
        assertEquals(1L, foundTicket.getId());
        assertEquals("Test Ticket", foundTicket.getTitle());
        verify(ticketRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTicketById_NotFound() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> ticketService.getTicketById(1L));
        verify(ticketRepository, times(1)).findById(1L);
    }
}