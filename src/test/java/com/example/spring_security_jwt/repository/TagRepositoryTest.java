package com.example.spring_security_jwt.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import com.example.entities.Tag;
import com.example.repository.TagRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName ("Test de TagRepository")
class TagRepositoryTest {

    @Autowired
    private TagRepository tagRepository;

    @Test
    void shouldFindTagByName() {
        // Arrange
        Tag tag = Tag.builder().name("Spring Boot").build();
        tagRepository.save(tag);

        // Act
        Optional<Tag> foundTag = tagRepository.findByName("Spring Boot");

        // Assert
        assertThat(foundTag).isPresent();
        assertThat(foundTag.get().getName()).isEqualTo("Spring Boot");
    }

    @Test
    void shouldFindTagsByNameContaining() {
        // Arrange
        Tag tag1 = Tag.builder().name("Java Basics").build();
        Tag tag2 = Tag.builder().name("Advanced Java").build();
        Tag tag3 = Tag.builder().name("Python").build();

        tagRepository.save(tag1);
        tagRepository.save(tag2);
        tagRepository.save(tag3);

        // Act
        List<Tag> foundTags = tagRepository.findByNameContaining("Java");

        // Assert
        assertThat(foundTags).hasSize(2);
        assertThat(foundTags).extracting(Tag::getName)
                .containsExactlyInAnyOrder("Java Basics", "Advanced Java");
    }

    @Test
    void shouldReturnEmptyWhenTagNotFound() {
        // Act
        Optional<Tag> foundTag = tagRepository.findByName("NonExistingTag");

        // Assert
        assertThat(foundTag).isEmpty();
    }
}
