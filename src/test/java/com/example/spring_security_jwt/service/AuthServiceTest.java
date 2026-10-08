package com.example.spring_security_jwt.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.spring_security_jwt.dto.JwtResponse;
import com.example.spring_security_jwt.dto.LoginRequest;
import com.example.spring_security_jwt.dto.MessageResponse;
import com.example.spring_security_jwt.dto.RegisterRequest;
import com.example.spring_security_jwt.model.ERole;
import com.example.spring_security_jwt.model.Role;
import com.example.spring_security_jwt.model.User;
import com.example.spring_security_jwt.repository.RoleRepository;
import com.example.spring_security_jwt.repository.UserRepository;
import com.example.spring_security_jwt.security.jwt.JwtUtils;
import com.example.spring_security_jwt.security.services.UserDetailsImpl;
import com.example.spring_security_jwt.service.impl.AuthServiceImpl;

@ExtendWith(MockitoExtension.class)
@DisplayName ("AuthServiceImpl test")
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void shouldLoginSuccessfully() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("juan@example.com");
        loginRequest.setPassword("password123");

        UserDetailsImpl userDetails = new UserDetailsImpl(
                1L,
                "juan123",
                "juan@example.com",
                "password123",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("mocked-jwt-token");

        // Act
        JwtResponse response = authService.login(loginRequest);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("mocked-jwt-token");
        assertThat(response.getUsername()).isEqualTo("juan123");
        assertThat(response.getEmail()).isEqualTo("juan@example.com");
        assertThat(response.getRoles()).contains("ROLE_USER");
    }

    @Test
    void shouldRegisterUserSuccessfullyWithDefaultRole() {
        // Arrange
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("maria123");
        registerRequest.setEmail("maria@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setRole(Collections.emptySet());

        Role defaultRole = Role.builder().id(1L).name(ERole.ROLE_USER).build();

        when(userRepository.existsByUsername("maria123")).thenReturn(false);
        when(userRepository.existsByEmail("maria@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(defaultRole));

        // Act
        MessageResponse response = authService.register(registerRequest);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getMessage()).isEqualTo("Usuario registrado correctamente");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenUsernameAlreadyExists() {
        // Arrange
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("existente");
        registerRequest.setEmail("nuevo@example.com");

        when(userRepository.existsByUsername("existente")).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> authService.register(registerRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El nombre de usuario ya está en uso");
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        // Arrange
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("nuevoUser");
        registerRequest.setEmail("existente@example.com");

        when(userRepository.existsByUsername("nuevoUser")).thenReturn(false);
        when(userRepository.existsByEmail("existente@example.com")).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> authService.register(registerRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El email ya está en uso");
    }

    @Test
    void shouldRegisterUserWithAdminRoleSuccessfully() {
        // Arrange
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("adminUser");
        registerRequest.setEmail("admin@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setRole(Set.of("admin"));

        Role adminRole = Role.builder().id(2L).name(ERole.ROLE_ADMIN).build();

        when(userRepository.existsByUsername("adminUser")).thenReturn(false);
        when(userRepository.existsByEmail("admin@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(roleRepository.findByName(ERole.ROLE_ADMIN)).thenReturn(Optional.of(adminRole));

        // Act
        MessageResponse response = authService.register(registerRequest);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getMessage()).isEqualTo("Usuario registrado correctamente");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenDefaultRoleNotFound() {
        // Arrange
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("sinRol");
        registerRequest.setEmail("sinrol@example.com");
        registerRequest.setPassword("password123");

        when(userRepository.existsByUsername("sinRol")).thenReturn(false);
        when(userRepository.existsByEmail("sinrol@example.com")).thenReturn(false);
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> authService.register(registerRequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("El rol ROLE_USER no existe");
    }

    @Test
    void shouldThrowExceptionWhenAdminRoleNotFound() {
        // Arrange
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("adminSinRol");
        registerRequest.setEmail("adminsinrol@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setRole(Set.of("admin"));

        when(userRepository.existsByUsername("adminSinRol")).thenReturn(false);
        when(userRepository.existsByEmail("adminsinrol@example.com")).thenReturn(false);
        when(roleRepository.findByName(ERole.ROLE_ADMIN)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> authService.register(registerRequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("El rol ROLE_ADMIN no existe");
    }
}