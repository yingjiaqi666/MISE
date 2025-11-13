package com.example.game.dto;

import com.example.game.entity.Option;

public class OptionDTO {
    private Long id;
    private String text;
    private String type;
    private int dc;
    private Long success;
    private Long fail;

    public OptionDTO(Option option) {
        this.id = option.getId();
        this.text = option.getText();
        this.type = option.getType();
        this.dc = option.getDc();
        this.success = option.getSuccess();
        this.fail = option.getFail();
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDc() {
        return dc;
    }

    public void setDc(int dc) {
        this.dc = dc;
    }

    public Long getSuccess() {
        return success;
    }

    public void setSuccess(Long success) {
        this.success = success;
    }

    public Long getFail() {
        return fail;
    }

    public void setFail(Long fail) {
        this.fail = fail;
    }
}
