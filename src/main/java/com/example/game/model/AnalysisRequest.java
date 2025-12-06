package com.example.game.model;

import java.util.List;

public class AnalysisRequest {
    private String sceneDescription;
    private String playerAction;
    private List<String> branches;

    // getters and setters
    public String getSceneDescription() { return sceneDescription; }
    public void setSceneDescription(String sceneDescription) { this.sceneDescription = sceneDescription; }

    public String getPlayerAction() { return playerAction; }
    public void setPlayerAction(String playerAction) { this.playerAction = playerAction; }

    public List<String> getBranches() { return branches; }
    public void setBranches(List<String> branches) { this.branches = branches; }
}
