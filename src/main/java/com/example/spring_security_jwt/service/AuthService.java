package com.example.spring_security_jwt.service;

import com.example.spring_security_jwt.dto.JwtResponse;
import com.example.spring_security_jwt.dto.LoginRequest;
import com.example.spring_security_jwt.dto.MessageResponse;
import com.example.spring_security_jwt.dto.RegisterRequest;

public interface AuthService {

    JwtResponse login(LoginRequest loginRequest);

    MessageResponse register(RegisterRequest registerRequest);
}
