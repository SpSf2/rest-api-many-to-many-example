package com.example.service;

import java.util.List;
import java.util.Optional;

import com.example.entities.Tutorial;

public interface TutorialService {
  
    Tutorial save(Tutorial tutorial);

    List<Tutorial> findAll();

    Optional<Tutorial> findById(Long id);

    void deleteById(Long id);

    List<Tutorial> findByPublished(boolean published);

    List<Tutorial> findByTitleContaining(String title);
}
