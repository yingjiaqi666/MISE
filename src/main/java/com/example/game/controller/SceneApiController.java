package com.example.game.controller;

import com.example.game.dto.ApiResponse;
import com.example.game.dto.SceneDTO;
import com.example.game.entity.Scene;
import com.example.game.entity.User;
import com.example.game.service.SceneService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/scenes")
public class SceneApiController {

    @Autowired
    private SceneService sceneService;
    @Autowired
    private com.example.game.service.OptionService optionService;

    @PostMapping("")
    public ResponseEntity<ApiResponse<Long>> createScene(@RequestBody Scene scene, HttpSession session) {
        User author = (User) session.getAttribute("user");
        if (author == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("401", "用户未登录"));
        }
        scene.setAuthor(author);
        Scene savedScene = sceneService.save(scene);
        return ResponseEntity.ok(ApiResponse.success(savedScene.getId(), "创建成功"));
    }

    @DeleteMapping("/{sceneId}")
    public ResponseEntity<ApiResponse<Void>> deleteScene(@PathVariable Long sceneId) {
        if (sceneService.findById(sceneId) == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("400", "场景不存在"));
        }
        sceneService.deleteById(sceneId);
        return ResponseEntity.ok(ApiResponse.success(null, "删除成功"));
    }

    @GetMapping("/{sceneId}")
    public ResponseEntity<ApiResponse<SceneDTO>> getSceneById(@PathVariable Long sceneId) {
        Scene scene = sceneService.findById(sceneId);
        if (scene == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("400", "场景不存在"));
        }
        return ResponseEntity.ok(ApiResponse.success(new SceneDTO(scene)));
    }

    @GetMapping("/{sceneId}/options")
    public ResponseEntity<ApiResponse<java.util.List<com.example.game.dto.OptionDTO>>> getOptionsByScene(@PathVariable Long sceneId) {
        java.util.List<com.example.game.entity.Option> options = optionService.findBySceneId(sceneId);
        java.util.List<com.example.game.dto.OptionDTO> dtos = options.stream().map(com.example.game.dto.OptionDTO::new).collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(dtos, "Options fetched"));
    }

    @PutMapping("/{sceneId}")
    public ResponseEntity<ApiResponse<Long>> updateScene(@PathVariable Long sceneId, @RequestBody Scene scene) {
        if (sceneService.findById(sceneId) == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("400", "场景不存在"));
        }
        Scene updated = sceneService.updateScene(sceneId, scene);
        if (updated == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("400", "更新失败"));
        }
        return ResponseEntity.ok(ApiResponse.success(updated.getId(), "更新成功"));
    }

    @GetMapping("/my-scenes")
    public ResponseEntity<ApiResponse<java.util.List<SceneDTO>>> getMyScenes(HttpSession session) {
        User author = (User) session.getAttribute("user");
        if (author == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("401", "用户未登录"));
        }
        java.util.List<Scene> scenes = sceneService.findByAuthorId(author.getId());
        java.util.List<SceneDTO> sceneDTOs = scenes.stream().map(SceneDTO::new).collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(sceneDTOs, "My scenes fetched successfully"));
    }
}
