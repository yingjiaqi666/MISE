package com.example.game.controller;

import com.example.game.dto.ApiResponse;
import com.example.game.dto.StoryDTO;
import com.example.game.entity.Story;
import com.example.game.entity.User;
import com.example.game.service.StoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/stories")
public class StoryApiController {

    @Autowired
    private StoryService storyService;
    @Autowired
    private com.example.game.service.SceneService sceneService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<StoryDTO>>> getAllStories() {
        List<Story> stories = storyService.findAll();
        List<StoryDTO> storyDTOs = stories.stream().map(StoryDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(storyDTOs, "Stories fetched successfully"));
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Long>> createStory(@RequestBody StoryCreateRequest request, HttpSession session) {
        User author = (User) session.getAttribute("user");
        if (author == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("401", "用户未登录"));
        }

        Story story = new Story();
        story.setName(request.getName());
        story.setIntroduction(request.getIntroduction());
        story.setCover(request.getCover());
        story.setMax(request.getMax());
        story.setAuthor(author);

        // If a firstSceneId is provided, validate it belongs to the current user and attach it
        if (request.getFirstSceneId() != null) {
            com.example.game.entity.Scene chosen = sceneService.findById(request.getFirstSceneId());
            if (chosen == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("400", "指定的场景不存在"));
            }
            if (chosen.getAuthor() == null || !chosen.getAuthor().getId().equals(author.getId())) {
                return ResponseEntity.status(403).body(ApiResponse.error("403", "无权将该场景作为起始场景"));
            }
            story.setFirstScene(chosen);
        }

        Story savedStory = storyService.save(story);
        return ResponseEntity.ok(ApiResponse.success(savedStory.getId(), "创建成功"));
    }

    @DeleteMapping("/{storyId}")
    public ResponseEntity<ApiResponse<Void>> deleteStory(@PathVariable Long storyId) {
        if (storyService.findById(storyId) == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("400", "故事不存在"));
        }
        storyService.deleteById(storyId);
        return ResponseEntity.ok(ApiResponse.success(null, "删除成功"));
    }

    @PostMapping("/search/name")
    public ResponseEntity<ApiResponse<List<StoryDTO>>> searchStoryByName(@RequestBody NameSearchRequest request) {
        List<Story> stories = storyService.findByName(request.getName());
        List<StoryDTO> storyDTOs = stories.stream().map(StoryDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(storyDTOs));
    }

    @PostMapping("/search/author")
    public ResponseEntity<ApiResponse<List<StoryDTO>>> searchStoryByAuthor(@RequestBody AuthorIdSearchRequest request) {
        List<Story> stories = storyService.findByAuthorId(request.getAuthorId());
        List<StoryDTO> storyDTOs = stories.stream().map(StoryDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(storyDTOs));
    }

    @GetMapping("/{storyId}")
    public ResponseEntity<ApiResponse<StoryDTO>> getStoryById(@PathVariable Long storyId) {
        Story story = storyService.findById(storyId);
        if (story == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("400", "故事不存在"));
        }
        return ResponseEntity.ok(ApiResponse.success(new StoryDTO(story)));
    }

    @PutMapping("/{storyId}")
    public ResponseEntity<ApiResponse<String>> updateStory(@PathVariable Long storyId, @RequestBody StoryCreateRequest request, HttpSession session) {
        User author = (User) session.getAttribute("user");
        if (author == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("401", "用户未登录"));
        }

        Story existingStory = storyService.findById(storyId);
        if (existingStory == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("400", "故事不存在"));
        }

        if (!existingStory.getAuthor().getId().equals(author.getId())) {
            return ResponseEntity.status(403).body(ApiResponse.error("403", "无权修改此故事"));
        }

        existingStory.setName(request.getName());
        existingStory.setIntroduction(request.getIntroduction());
        existingStory.setCover(request.getCover());
        existingStory.setMax(request.getMax());
        // Handle optional firstScene change
        if (request.getFirstSceneId() != null) {
            com.example.game.entity.Scene chosen = sceneService.findById(request.getFirstSceneId());
            if (chosen == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("400", "指定的场景不存在"));
            }
            if (chosen.getAuthor() == null || !chosen.getAuthor().getId().equals(author.getId())) {
                return ResponseEntity.status(403).body(ApiResponse.error("403", "无权将该场景作为起始场景"));
            }
            existingStory.setFirstScene(chosen);
        } else {
            // if explicitly null was sent, clear firstScene; otherwise keep existing
            // Here we interpret null as "no change" for safety.
        }

        storyService.updateStory(storyId, existingStory);
        return ResponseEntity.ok(ApiResponse.success(null, "更新成功"));
    }

    @GetMapping("/my-stories")
    public ResponseEntity<ApiResponse<List<StoryDTO>>> getMyStories(HttpSession session) {
        User author = (User) session.getAttribute("user");
        if (author == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("401", "用户未登录"));
        }

        List<Story> stories = storyService.findByAuthorId(author.getId());
        List<StoryDTO> storyDTOs = stories.stream().map(StoryDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(storyDTOs, "My stories fetched successfully"));
    }

    static class NameSearchRequest {
        private String name;
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    static class StoryCreateRequest {
        private String name;
        private String introduction;
        private String cover;
        private int max;
        private Long firstSceneId;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getIntroduction() { return introduction; }
        public void setIntroduction(String introduction) { this.introduction = introduction; }
        public String getCover() { return cover; }
        public void setCover(String cover) { this.cover = cover; }
        public int getMax() { return max; }
        public void setMax(int max) { this.max = max; }
        public Long getFirstSceneId() { return firstSceneId; }
        public void setFirstSceneId(Long firstSceneId) { this.firstSceneId = firstSceneId; }
    }

    static class AuthorIdSearchRequest {
        private Long authorId;
        public Long getAuthorId() { return authorId; }
        public void setAuthorId(Long authorId) { this.authorId = authorId; }
    }
}
