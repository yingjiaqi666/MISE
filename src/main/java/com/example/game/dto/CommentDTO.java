package com.example.game.dto;

import com.example.game.entity.Comment;

public class CommentDTO {
    private Long id;
    private String text;
    private String picture;
    private Long authorId;

    public CommentDTO(Comment comment) {
        this.id = comment.getId();
        this.text = comment.getText();
        this.picture = comment.getPicture();
        if (comment.getAuthor() != null) {
            this.authorId = comment.getAuthor().getId();
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }
}
