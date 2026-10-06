package com.example.spring_security_jwt.dto;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    @Size(min = 3, max = 20, message = "El usuario debe tener entre 3 y 20 caracteres")
    private String username;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "Debe proporcionar un email válido")
    @Size(max = 50, message = "El email no puede superar los 50 caracteres")
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 6, max = 40, message = "La contraseña debe tener entre 6 y 40 caracteres")
    private String password;

    private Set<String> role;
}
