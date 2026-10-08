package com.example.service;

import java.util.List;
import java.util.Optional;

import com.example.entities.Tag;

public interface TagService {
    
    Tag save(Tag tag);

    List<Tag> findAll();

    Optional<Tag> findById(Long id);

    Optional<Tag> findByName(String name);

    List<Tag> findByNameContaining(String name);

    void deleteById(Long id);
}
