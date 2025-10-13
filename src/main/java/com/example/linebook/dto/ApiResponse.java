package com.example.linebook.dto;

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
        this.message = message;
        this.data = data;
    }

    // Factory methods for simplicity
    public static <T> ApiResponse<T> success(String code, String message, T data) {
        return new ApiResponse<>( code, message, data);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("SUCCESS", "SUCCESS", data);
    }

    public static <T> ApiResponse<T> error(String code) {
         String message = code;
         // TODO eroor message
        return new ApiResponse<>( code, message, null);
    }

    public static void writeReponseError(HttpServletResponse response, int status,String code) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(ApiResponse.error(code)));
    }

}
