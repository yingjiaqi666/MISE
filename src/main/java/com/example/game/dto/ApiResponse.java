package com.example.game.dto;

public class ApiResponse<T> {
    private String code;
    private T data;
    private String msg;

    public ApiResponse(String code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public static <T> ApiResponse<T> success(T data, String msg) {
        return new ApiResponse<>("200", data, msg);
    }
    
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("200", data, "success");
    }

    public static <T> ApiResponse<T> error(String code, String msg) {
        return new ApiResponse<>(code, null, msg);
    }

    // Getters and Setters
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
