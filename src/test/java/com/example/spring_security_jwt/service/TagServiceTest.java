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

import com.example.entities.Tag;
import com.example.repository.TagRepository;
import com.example.service.impl.TagServiceImpl;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagServiceImpl tagService;

    @Test
    void shouldSaveTag() {
        // Arrange
        Tag tag = Tag.builder().name("Spring Boot").build();
        when(tagRepository.save(any(Tag.class))).thenReturn(tag);

        // Act
        Tag savedTag = tagService.save(tag);

        // Assert
        assertThat(savedTag).isNotNull();
        assertThat(savedTag.getName()).isEqualTo("Spring Boot");
        verify(tagRepository).save(tag);
    }

    @Test
    void shouldFindTagByName() {
        // Arrange
        Tag tag = Tag.builder().id(1L).name("Java").build();
        when(tagRepository.findByName("Java")).thenReturn(Optional.of(tag));

        // Act
        Optional<Tag> found = tagService.findByName("Java");

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Java");
        verify(tagRepository).findByName("Java");
    }
}