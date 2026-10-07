package com.example.spring_security_jwt.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring_security_jwt.dto.JwtResponse;
import com.example.spring_security_jwt.dto.LoginRequest;
import com.example.spring_security_jwt.dto.MessageResponse;
import com.example.spring_security_jwt.dto.RegisterRequest;
import com.example.spring_security_jwt.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(
            @Valid @RequestBody LoginRequest loginRequest) {

        JwtResponse jwtResponse = authService.login(loginRequest);

        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> registerUser(
            @Valid @RequestBody RegisterRequest registerRequest) {

        MessageResponse messageResponse =
                authService.register(registerRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(messageResponse);
    }
}