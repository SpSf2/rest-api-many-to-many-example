package com.example.repository;

import com.example.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    List<Tag> findTagsByTutorialsId(Long tutorialId);

    Optional<Tag> findByName(String name);

    List<Tag> findByNameContaining(String name);

}
