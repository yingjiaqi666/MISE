package com.example.game.dto;

public class PlayResult {
    private Long nextSceneId;
    private int roll; // d20
    private int modifier; // attribute value
    private int total;
    private boolean success;

    public PlayResult() {}

    public PlayResult(Long nextSceneId, int roll, int modifier, int total, boolean success) {
        this.nextSceneId = nextSceneId;
        this.roll = roll;
        this.modifier = modifier;
        this.total = total;
        this.success = success;
    }

    public Long getNextSceneId() {
        return nextSceneId;
    }

    public void setNextSceneId(Long nextSceneId) {
        this.nextSceneId = nextSceneId;
    }

    public int getRoll() {
        return roll;
    }

    public void setRoll(int roll) {
        this.roll = roll;
    }

    public int getModifier() {
        return modifier;
    }

    public void setModifier(int modifier) {
        this.modifier = modifier;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
