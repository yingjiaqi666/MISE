package com.example.game.repository;

import com.example.game.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StoryRepository extends JpaRepository<Story, Long> {
    List<Story> findByNameContaining(String name);
    List<Story> findByAuthorId(Long authorId);
}
