package com.example.linebook.dto;

import com.example.linebook.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Data
public class ApiResponse<T> {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    private String code;
    private String message;
    private T data;

    public ApiResponse(String code, String message, T data) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public ApiResponse(String code, T data) {
        this.code = code;
        this.data = data;
    }



    // Factory methods for simplicity
    public static <T> ApiResponse<T> success(String code, String message, T data) {
        return new ApiResponse<>( code, data);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("SUCCESS", data);
    }

    public static <T> ApiResponse<T> error(ErrorCode errorCode) {
        return new ApiResponse<>(errorCode.getCode(), errorCode.getMessage(), null);
    }

    public static <T> ApiResponse<T> error(ErrorCode errorCode, String message) {
        return new ApiResponse<>(errorCode.getCode(), message, null);
    }


}
