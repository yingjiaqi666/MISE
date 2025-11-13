package com.example.game.dto;

import com.example.game.entity.Comment;
import com.example.game.entity.Story;

import java.util.List;
import java.util.stream.Collectors;

public class StoryDTO {
    private Long id;
    private Long authorId;
    private String authorName;
    private int max;
    private String name;
    private String introduction;
    private String cover;
    private Long firstSceneId;
    private List<Long> comments;

    public StoryDTO(Story story) {
        this.id = story.getId();
        if (story.getAuthor() != null) {
            this.authorId = story.getAuthor().getId();
            this.authorName = story.getAuthor().getUsername();
        }
        this.max = story.getMax();
        this.name = story.getName();
        this.introduction = story.getIntroduction();
        this.cover = story.getCover();
        if (story.getFirstScene() != null) {
            this.firstSceneId = story.getFirstScene().getId();
        }
        if (story.getComments() != null) {
            this.comments = story.getComments().stream().map(Comment::getId).collect(Collectors.toList());
        }
    }

    public StoryDTO() {
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Long getFirstSceneId() {
        return firstSceneId;
    }

    public void setFirstSceneId(Long firstSceneId) {
        this.firstSceneId = firstSceneId;
    }

    public List<Long> getComments() {
        return comments;
    }

    public void setComments(List<Long> comments) {
        this.comments = comments;
    }
}
