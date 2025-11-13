package com.example.game.service;

import com.example.game.entity.Story;
import com.example.game.repository.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoryService {

    @Autowired
    private StoryRepository storyRepository;

    public Story save(Story story) {
        return storyRepository.save(story);
    }

    public List<Story> findAll() {
        return storyRepository.findAll();
    }

    public Story findById(Long id) {
        return storyRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        storyRepository.deleteById(id);
    }

    public List<Story> findByName(String name) {
        return storyRepository.findByNameContaining(name);
    }

    public List<Story> findByAuthorId(Long authorId) {
        return storyRepository.findByAuthorId(authorId);
    }

    public Story updateStory(Long id, Story storyDetails) {
        Story story = findById(id);
        if (story != null) {
            story.setName(storyDetails.getName());
            story.setIntroduction(storyDetails.getIntroduction());
            story.setCover(storyDetails.getCover());
            story.setMax(storyDetails.getMax());
            // firstScene and comments might need special handling
            return storyRepository.save(story);
        }
        return null;
    }
}
