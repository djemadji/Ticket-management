package com.test.ticketmanagement.controller;

import com.test.ticketmanagement.api.model.TicketResponse;
import com.test.ticketmanagement.api.model.UserRequest;
import com.test.ticketmanagement.api.model.UserResponse;
import com.test.ticketmanagement.service.UserService;
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

class UserApiDelegateImplTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserApiDelegateImpl userApiDelegate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() {
        List<UserResponse> users = Arrays.asList(new UserResponse(), new UserResponse());
        when(userService.getAllUsers()).thenReturn(users);

        ResponseEntity<List<UserResponse>> result = userApiDelegate.getAllUsers();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(users, result.getBody());
    }

    @Test
    void testGetUserById() {
        Long id = 1L;
        UserResponse response = new UserResponse();
        when(userService.getUserById(id)).thenReturn(response);

        ResponseEntity<UserResponse> result = userApiDelegate.getUserById(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void testCreateUser() {
        UserRequest request = new UserRequest();
        UserResponse response = new UserResponse();
        when(userService.createUser(request)).thenReturn(response);

        ResponseEntity<UserResponse> result = userApiDelegate.createUser(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void testUpdateUser() {
        Long id = 1L;
        UserRequest request = new UserRequest();
        UserResponse response = new UserResponse();
        when(userService.updateUser(id, request)).thenReturn(response);

        ResponseEntity<UserResponse> result = userApiDelegate.updateUser(id, request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void testDeleteUser() {
        Long id = 1L;

        ResponseEntity<Void> result = userApiDelegate.deleteUser(id);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(userService).deleteUser(id);
    }

    @Test
    void testGetUserTickets() {
        Long id = 1L;
        List<TicketResponse> tickets = Arrays.asList(new TicketResponse(), new TicketResponse());
        when(userService.getUserTickets(id)).thenReturn(tickets);

        ResponseEntity<List<TicketResponse>> result = userApiDelegate.getUserTickets(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(tickets, result.getBody());
    }
}