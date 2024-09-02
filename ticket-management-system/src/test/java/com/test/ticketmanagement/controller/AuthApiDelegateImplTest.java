package com.test.ticketmanagement.controller;

import com.test.ticketmanagement.api.model.AuthenticationRequest;
import com.test.ticketmanagement.api.model.AuthenticationResponse;
import com.test.ticketmanagement.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class AuthApiDelegateImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private JwtUtil jwtTokenUtil;

    @InjectMocks
    private AuthApiDelegateImpl authApiDelegate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoginUser_Success() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername("testuser");
        request.setPassword("password");

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);
        when(jwtTokenUtil.generateToken(userDetails)).thenReturn("testToken");

        ResponseEntity<AuthenticationResponse> response = authApiDelegate.loginUser(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("testToken", response.getBody().getJwt());
    }

    @Test
    void testLoginUser_Failure() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername("testuser");
        request.setPassword("wrongpassword");

        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad credentials"));

        ResponseEntity<AuthenticationResponse> response = authApiDelegate.loginUser(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}