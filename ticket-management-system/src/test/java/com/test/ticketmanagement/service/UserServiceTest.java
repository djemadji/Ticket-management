package com.test.ticketmanagement.service;

import com.test.ticketmanagement.api.model.UserRequest;
import com.test.ticketmanagement.api.model.UserResponse;
import com.test.ticketmanagement.exception.ResourceNotFoundException;
import com.test.ticketmanagement.mapper.UserMapper;
import com.test.ticketmanagement.model.User;
import com.test.ticketmanagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser() {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("testuser");
        userRequest.setEmail("test@example.com");
        userRequest.setPassword("password");

        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");

        UserResponse userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setUsername("testuser");
        userResponse.setEmail("test@example.com");

        when(userMapper.toEntity(any(UserRequest.class))).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toResponse(any(User.class))).thenReturn(userResponse);

        UserResponse createdUser = userService.createUser(userRequest);

        assertNotNull(createdUser);
        assertEquals(1L, createdUser.getId());
        assertEquals("testuser", createdUser.getUsername());
        assertEquals("test@example.com", createdUser.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testGetUserById() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");

        UserResponse userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setUsername("testuser");
        userResponse.setEmail("test@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse foundUser = userService.getUserById(1L);

        assertNotNull(foundUser);
        assertEquals(1L, foundUser.getId());
        assertEquals("testuser", foundUser.getUsername());
        assertEquals("test@example.com", foundUser.getEmail());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(1L));
        verify(userRepository, times(1)).findById(1L);
    }
}