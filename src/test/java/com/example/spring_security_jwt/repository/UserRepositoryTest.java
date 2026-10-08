package com.example.spring_security_jwt.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import com.example.spring_security_jwt.model.ERole;
import com.example.spring_security_jwt.model.Role;
import com.example.spring_security_jwt.model.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName ("Test de UserRepository")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void shouldFindUserByEmail() {
        // Arrange
        Role roleUser = roleRepository.save(
                Role.builder().name(ERole.ROLE_USER).build()
        );

        User user = User.builder()
                .username("juan123")
                .email("juan@example.com")
                .password("password123")
                .roles(Set.of(roleUser))
                .build();

        userRepository.save(user);

        // Act
        Optional<User> foundUser = userRepository.findByEmail("juan@example.com");

        // Assert
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("juan123");
        assertThat(foundUser.get().getEmail()).isEqualTo("juan@example.com");
    }

    @Test
    void shouldReturnTrueWhenEmailExists() {
        // Arrange
        User user = User.builder()
                .username("maria456")
                .email("maria@example.com")
                .password("password123")
                .build();

        userRepository.save(user);

        // Act
        Boolean exists = userRepository.existsByEmail("maria@example.com");

        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    void shouldReturnFalseWhenEmailDoesNotExist() {
        // Act
        Boolean exists = userRepository.existsByEmail("noexiste@example.com");

        // Assert
        assertThat(exists).isFalse();
    }
}