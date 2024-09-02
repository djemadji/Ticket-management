package com.test.ticketmanagement.service;

import com.test.ticketmanagement.api.model.TicketRequest;
import com.test.ticketmanagement.api.model.TicketResponse;
import com.test.ticketmanagement.exception.InvalidOperationException;
import com.test.ticketmanagement.exception.ResourceNotFoundException;
import com.test.ticketmanagement.mapper.TicketMapper;
import com.test.ticketmanagement.model.Ticket;
import com.test.ticketmanagement.model.TicketStatus;
import com.test.ticketmanagement.model.User;
import com.test.ticketmanagement.repository.TicketRepository;
import com.test.ticketmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final TicketMapper ticketMapper;

    @Transactional
    public TicketResponse createTicket(TicketRequest ticketRequest) {
        User assignedUser = userRepository.findById(ticketRequest.getAssignedUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + ticketRequest.getAssignedUserId()));

        Ticket ticket = ticketMapper.toEntity(ticketRequest);
        ticket.setAssignedUser(assignedUser);

        Ticket savedTicket = ticketRepository.save(ticket);
        return ticketMapper.toResponse(savedTicket);
    }

    @Transactional(readOnly = true)
    public List<TicketResponse> getAllTickets() {
        return ticketRepository.findAll().stream()
                .map(ticketMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TicketResponse getTicketById(Long id) {
        return ticketRepository.findById(id)
                .map(ticketMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + id));
    }

    @Transactional
    public TicketResponse updateTicket(Long id, TicketRequest ticketRequest) {
        return ticketRepository.findById(id)
                .map(ticket -> {
                    if (ticket.getStatus() == TicketStatus.CANCELLED) {
                        throw new InvalidOperationException("Cannot update a cancelled ticket");
                    }

                    User assignedUser = userRepository.findById(ticketRequest.getAssignedUserId())
                            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + ticketRequest.getAssignedUserId()));
                    ticketMapper.updateTicketFromRequest(ticketRequest, ticket);
                    ticket.setAssignedUser(assignedUser);
                    return ticketMapper.toResponse(ticketRepository.save(ticket));
                })
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + id));
    }

    @Transactional
    public void deleteTicket(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + id));
        ticketRepository.delete(ticket);
    }

    @Transactional
    public TicketResponse assignTicket(Long ticketId, Long userId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + ticketId));

        if (ticket.getStatus() == TicketStatus.COMPLETED ||
                ticket.getStatus() == TicketStatus.CANCELLED) {
            throw new InvalidOperationException("Cannot assign a completed or cancelled ticket");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        ticket.setAssignedUser(user);
        return ticketMapper.toResponse(ticketRepository.save(ticket));
    }
}