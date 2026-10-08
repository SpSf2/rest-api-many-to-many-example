package com.example.spring_security_jwt.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.entities.Tutorial;
import com.example.repository.TutorialRepository;
import com.example.spring_security_jwt.security.jwt.AuthEntryPointJwt;
import com.example.spring_security_jwt.security.jwt.JwtUtils;
import com.example.spring_security_jwt.security.services.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("TutorialController test")
class TutorialControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TutorialRepository tutorialRepository;

    // Beans de seguridad mockeados para que Spring Security no falle al levantar el ApplicationContext
    @MockitoBean
    private JwtUtils jwtUtils;

    @MockitoBean
    private AuthEntryPointJwt unauthorizedHandler;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockUser
    void shouldReturnAllTutorials() throws Exception {
        Tutorial tut1 = Tutorial.builder().id(1L).title("Java").description("Desc 1").published(true).build();
        Tutorial tut2 = Tutorial.builder().id(2L).title("Spring").description("Desc 2").published(true).build();

        when(tutorialRepository.findAll()).thenReturn(List.of(tut1, tut2));

        mockMvc.perform(get("/api/tutorials"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Java"))
                .andExpect(jsonPath("$[1].title").value("Spring"));
    }

    @Test
    @WithMockUser
    void shouldReturnNoContentWhenGetAllTutorialsIsEmpty() throws Exception {
        when(tutorialRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/tutorials"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void shouldReturnTutorialById() throws Exception {
        Tutorial tut = Tutorial.builder().id(1L).title("Spring Boot").description("Desc Boot").published(true).build();

        when(tutorialRepository.findById(1L)).thenReturn(Optional.of(tut));

        mockMvc.perform(get("/api/tutorials/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Spring Boot"));
    }

    @Test
    @WithMockUser
    void shouldCreateTutorial() throws Exception {
        Tutorial tutorialToSave = Tutorial.builder().title("Angular").description("Curso Web").build();
        Tutorial savedTutorial = Tutorial.builder().id(10L).title("Angular").description("Curso Web").published(true).build();

        when(tutorialRepository.save(any(Tutorial.class))).thenReturn(savedTutorial);

        mockMvc.perform(post("/api/tutorials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tutorialToSave)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.title").value("Angular"))
                .andExpect(jsonPath("$.published").value(true));
    }

    @Test
    @WithMockUser
    void shouldUpdateTutorial() throws Exception {
        Tutorial existingTutorial = Tutorial.builder().id(1L).title("Java Original").description("Desc Original").published(false).build();
        Tutorial updatedTutorial = Tutorial.builder().id(1L).title("Java Modificado").description("Desc Modificada").published(true).build();

        when(tutorialRepository.findById(1L)).thenReturn(Optional.of(existingTutorial));
        when(tutorialRepository.save(any(Tutorial.class))).thenReturn(updatedTutorial);

        mockMvc.perform(put("/api/tutorials/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTutorial)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Java Modificado"))
                .andExpect(jsonPath("$.published").value(true));
    }

    @Test
    @WithMockUser
    void shouldDeleteTutorial() throws Exception {
        doNothing().when(tutorialRepository).deleteById(1L);

        mockMvc.perform(delete("/api/tutorials/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void shouldReturnPublishedTutorials() throws Exception {
        Tutorial tut = Tutorial.builder().id(1L).title("Spring Security").description("JWT").published(true).build();

        when(tutorialRepository.findByPublished(true)).thenReturn(List.of(tut));

        mockMvc.perform(get("/api/tutorials/published"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].published").value(true));
    }
}