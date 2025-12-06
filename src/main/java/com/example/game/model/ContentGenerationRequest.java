package com.example.game.model;

import java.util.Map;

public class ContentGenerationRequest {
    private String contentType; // story, description, dialogue, character
    private String theme;
    private Map<String, String> parameters;

    // getters and setters
    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }

    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }

    public Map<String, String> getParameters() { return parameters; }
    public void setParameters(Map<String, String> parameters) { this.parameters = parameters; }
}