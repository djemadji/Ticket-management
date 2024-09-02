package com.test.ticketmanagement.service;

import com.test.ticketmanagement.api.model.TicketResponse;
import com.test.ticketmanagement.api.model.UserRequest;
import com.test.ticketmanagement.api.model.UserResponse;
import com.test.ticketmanagement.exception.InvalidOperationException;
import com.test.ticketmanagement.exception.ResourceNotFoundException;
import com.test.ticketmanagement.mapper.TicketMapper;
import com.test.ticketmanagement.mapper.UserMapper;
import com.test.ticketmanagement.model.User;
import com.test.ticketmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final TicketMapper ticketMapper;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public UserResponse createUser(UserRequest userRequest) {
        if (userRepository.existsByUsername(userRequest.getUsername())) {
            throw new InvalidOperationException("Username already taken");
        }
        User user = userMapper.toEntity(userRequest);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Transactional
    public UserResponse updateUser(Long id, UserRequest userRequest) {
        return userRepository.findById(id)
                .map(user -> {
                    if (userRequest.getUsername() != null &&
                            !user.getUsername().equals(userRequest.getUsername()) &&
                            userRepository.existsByUsername(userRequest.getUsername())) {
                        throw new InvalidOperationException("Username already taken");
                    }
                    userMapper.updateUserFromRequest(userRequest, user);
                    return userMapper.toResponse(userRepository.save(user));
                })
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if (!user.getTickets().isEmpty()) {
            throw new InvalidOperationException("Cannot delete user with assigned tickets");
        }

        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    public List<TicketResponse> getUserTickets(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return user.getTickets().stream()
                .map(ticketMapper::toResponse)
                .collect(Collectors.toList());
    }
}