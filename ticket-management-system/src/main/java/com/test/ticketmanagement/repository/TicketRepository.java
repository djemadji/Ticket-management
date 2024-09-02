package com.test.ticketmanagement.repository;

import com.test.ticketmanagement.model.Ticket;
import com.test.ticketmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByAssignedUser(User user);
}
