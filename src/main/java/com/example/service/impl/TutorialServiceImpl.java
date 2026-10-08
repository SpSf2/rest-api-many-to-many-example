package com.example.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.entities.Tutorial;
import com.example.repository.TutorialRepository;
import com.example.service.TutorialService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TutorialServiceImpl implements TutorialService {

    private final TutorialRepository tutorialRepository;

    @Override
    public Tutorial save(Tutorial tutorial) {
        return tutorialRepository.save(tutorial);
    }

    @Override
    public List<Tutorial> findAll() {
        return tutorialRepository.findAll();
    }

    @Override
    public Optional<Tutorial> findById(Long id) {
        return tutorialRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        tutorialRepository.deleteById(id);
    }

    @Override
    public List<Tutorial> findByPublished(boolean published) {
        return tutorialRepository.findByPublished(published);
    }

    @Override
    public List<Tutorial> findByTitleContaining(String title) {
        return tutorialRepository.findByTitleContaining(title);
    }
}
