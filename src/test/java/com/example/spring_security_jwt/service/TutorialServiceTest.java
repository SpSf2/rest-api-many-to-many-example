package com.example.spring_security_jwt.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.entities.Tutorial;
import com.example.repository.TutorialRepository;

@ExtendWith(MockitoExtension.class)
class TutorialServiceTest {

    @Mock
    private TutorialRepository tutorialRepository;

    @InjectMocks
    private TutorialServiceImpl tutorialService;

    @Test
    void shouldCreateTutorial() {
        // Arrange
        Tutorial tutorial = Tutorial.builder()
                .title("Spring Boot")
                .description("Curso completo")
                .published(true)
                .build();

        when(tutorialRepository.save(any(Tutorial.class))).thenReturn(tutorial);

        // Act
        Tutorial created = tutorialService.save(tutorial);

        // Assert
        assertThat(created).isNotNull();
        assertThat(created.getTitle()).isEqualTo("Spring Boot");
        verify(tutorialRepository).save(tutorial);
    }

    @Test
    void shouldFindTutorialById() {
        // Arrange
        Tutorial tutorial = Tutorial.builder()
                .id(1L)
                .title("Spring Security")
                .description("JWT desde cero")
                .published(true)
                .build();

        when(tutorialRepository.findById(1L)).thenReturn(Optional.of(tutorial));

        // Act
        Optional<Tutorial> found = tutorialService.findById(1L);

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Spring Security");
        verify(tutorialRepository).findById(1L);
    }
}