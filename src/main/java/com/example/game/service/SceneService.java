package com.example.game.service;

import com.example.game.entity.Scene;
import com.example.game.repository.SceneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SceneService {

    @Autowired
    private SceneRepository sceneRepository;

    public Scene save(Scene scene) {
        return sceneRepository.save(scene);
    }

    public java.util.List<Scene> findByAuthorId(Long authorId) {
        return sceneRepository.findByAuthorId(authorId);
    }

    public Scene findById(Long id) {
        return sceneRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        sceneRepository.deleteById(id);
    }

    public Scene updateScene(Long id, Scene sceneDetails) {
        Scene scene = findById(id);
        if (scene != null) {
            scene.setName(sceneDetails.getName());
            scene.setText(sceneDetails.getText());
            scene.setPicture(sceneDetails.getPicture());
            // options, comments, products might need special handling
            return sceneRepository.save(scene);
        }
        return null;
    }
}
