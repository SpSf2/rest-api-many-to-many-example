package com.example.spring_security_jwt.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import com.example.spring_security_jwt.model.ERole;
import com.example.spring_security_jwt.model.Role;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName ("Test de RoleRepository")
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void shouldFindRoleByName() {

        Role role = Role.builder()
                .name(ERole.ROLE_ADMIN)
                .build();

        roleRepository.save(role);

        Optional<Role> foundRole =
                roleRepository.findByName(ERole.ROLE_ADMIN);

        assertThat(foundRole).isPresent();
        assertThat(foundRole.get().getName())
                .isEqualTo(ERole.ROLE_ADMIN);
    }

    @Test
    void shouldReturnEmptyWhenRoleDoesNotExist() {

        Optional<Role> foundRole =
                roleRepository.findByName(ERole.ROLE_ADMIN);

        assertThat(foundRole).isEmpty();
    }
}
