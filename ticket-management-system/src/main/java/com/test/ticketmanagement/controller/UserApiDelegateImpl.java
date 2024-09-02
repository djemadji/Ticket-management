package com.test.ticketmanagement.controller;

import com.test.ticketmanagement.api.UserApiDelegate;
import com.test.ticketmanagement.api.model.TicketResponse;
import com.test.ticketmanagement.api.model.UserRequest;
import com.test.ticketmanagement.api.model.UserResponse;
import com.test.ticketmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserApiDelegateImpl implements UserApiDelegate {

    private final UserService userService;

    @Override
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @Override
    public ResponseEntity<UserResponse> getUserById(Long id) {
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @Override
    public ResponseEntity<UserResponse> createUser(UserRequest userRequest) {
        UserResponse createdUser = userService.createUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @Override
    public ResponseEntity<UserResponse> updateUser(Long id, UserRequest userRequest) {
        UserResponse updatedUser = userService.updateUser(id, userRequest);
        return ResponseEntity.ok(updatedUser);
    }

    @Override
    public ResponseEntity<Void> deleteUser(Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<TicketResponse>> getUserTickets(Long id) {
        List<TicketResponse> tickets = userService.getUserTickets(id);
        return ResponseEntity.ok(tickets);
    }
}