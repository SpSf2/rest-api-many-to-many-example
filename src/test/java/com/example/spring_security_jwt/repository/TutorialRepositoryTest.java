package com.example.spring_security_jwt.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import com.example.entities.Tag;
import com.example.entities.Tutorial;
import com.example.repository.TagRepository;
import com.example.repository.TutorialRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName ("Test de TutorialRepository")
class TutorialRepositoryTest {

    @Autowired
    private TutorialRepository tutorialRepository;

    @Autowired
    private TagRepository tagRepository;

    @Test
    void shouldSaveTutorialWithTags() {
        // Arrange
        Tag tagJava = tagRepository.save(Tag.builder().name("Java").build());
        
        Tutorial tutorial = Tutorial.builder()
                .title("Tutorial de Spring Boot")
                .description("Aprende Spring Boot desde cero")
                .published(true)
                .build();
        
        tutorial.addTag(tagJava);

        // Act
        Tutorial savedTutorial = tutorialRepository.save(tutorial);

        // Assert
        assertThat(savedTutorial.getId()).isNotNull();
        assertThat(savedTutorial.getTags()).hasSize(1);
        assertThat(savedTutorial.getTags().iterator().next().getName()).isEqualTo("Java");
    }

    @Test
    void shouldFindByPublishedTrue() {
        // Arrange
        Tutorial tut1 = Tutorial.builder().title("Tut 1").description("Desc 1").published(true).build();
        Tutorial tut2 = Tutorial.builder().title("Tut 2").description("Desc 2").published(false).build();
        
        tutorialRepository.save(tut1);
        tutorialRepository.save(tut2);

        // Act
        List<Tutorial> publishedTutorials = tutorialRepository.findByPublished(true);

        // Assert
        assertThat(publishedTutorials).hasSize(1);
        assertThat(publishedTutorials.get(0).getTitle()).isEqualTo("Tut 1");
    }

    @Test
    void shouldFindByTitleContaining() {
        // Arrange
        Tutorial tut = Tutorial.builder()
                .title("Aprende JWT paso a paso")
                .description("Guía de seguridad")
                .published(true)
                .build();
        
        tutorialRepository.save(tut);

        // Act
        List<Tutorial> foundTutorials = tutorialRepository.findByTitleContaining("JWT");

        // Assert
        assertThat(foundTutorials).isNotEmpty();
        assertThat(foundTutorials.get(0).getTitle()).contains("JWT");
    }
}
