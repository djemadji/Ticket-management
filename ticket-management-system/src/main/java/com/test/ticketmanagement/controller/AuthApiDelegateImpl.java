package com.test.ticketmanagement.controller;

import com.test.ticketmanagement.api.AuthApiDelegate;
import com.test.ticketmanagement.api.model.AuthenticationRequest;
import com.test.ticketmanagement.api.model.AuthenticationResponse;
import com.test.ticketmanagement.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;


@Component public class AuthApiDelegateImpl implements AuthApiDelegate {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtTokenUtil;

    public AuthApiDelegateImpl(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtUtil jwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public ResponseEntity<AuthenticationResponse> loginUser( AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).build();
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);

        AuthenticationResponse response = new AuthenticationResponse();
        response.setJwt(jwt);

        return ResponseEntity.ok(response);
    }
}