package com.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.spring_security_jwt.model.ERole;
import com.example.spring_security_jwt.model.Role;
import com.example.spring_security_jwt.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreateSamplesData implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        // 1. Inicializar roles en la base de datos si no existen
        if (!roleRepository.findByName(ERole.ROLE_USER).isPresent()) {
            roleRepository.save(Role.builder()
                    .name(ERole.ROLE_USER)
                    .build());
            System.out.println("Rol ROLE_USER creado exitosamente.");
        }

        if (!roleRepository.findByName(ERole.ROLE_ADMIN).isPresent()) {
            roleRepository.save(Role.builder()
                    .name(ERole.ROLE_ADMIN)
                    .build());
            System.out.println("Rol ROLE_ADMIN creado exitosamente.");
        }
    }
}
