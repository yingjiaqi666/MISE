package com.example.game.dto;

import com.example.game.entity.Scene;

import java.util.List;
import java.util.stream.Collectors;

public class SceneDTO {
    private Long id;
    private String name;
    private String text;
    private String picture;
    private List<Long> options;
    private List<Long> comments;
    private List<Long> products;

    public SceneDTO(Scene scene) {
        this.id = scene.getId();
        this.name = scene.getName();
        this.text = scene.getText();
        this.picture = scene.getPicture();
        if (scene.getOptions() != null) {
            this.options = scene.getOptions().stream().map(o -> o.getId()).collect(Collectors.toList());
        }
        if (scene.getComments() != null) {
            this.comments = scene.getComments().stream().map(c -> c.getId()).collect(Collectors.toList());
        }
        if (scene.getProducts() != null) {
            this.products = scene.getProducts().stream().map(p -> p.getId()).collect(Collectors.toList());
        }
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<Long> getOptions() {
        return options;
    }

    public void setOptions(List<Long> options) {
        this.options = options;
    }

    public List<Long> getComments() {
        return comments;
    }

    public void setComments(List<Long> comments) {
        this.comments = comments;
    }

    public List<Long> getProducts() {
        return products;
    }

    public void setProducts(List<Long> products) {
        this.products = products;
    }
}
