package com.example.linebook.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Data
public class ApiResponse<T> {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    private boolean success;
    private String code;    // e.g. "USER_001" or "SYS_500"
    private String message; // human-readable message
    private T data;


    public ApiResponse(boolean success, String code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // Factory methods for simplicity
    public static <T> ApiResponse<T> success(String code, String message, T data) {
        return new ApiResponse<>(true, code, message, data);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "SUCCESS", "OK", data);
    }

    public static <T> ApiResponse<T> error(String code) {
         String message = "error";
         // TODO eroor message
        return new ApiResponse<>(false, code, message, null);
    }

    public static void writeReponseError(HttpServletResponse response, int status,String code) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(ApiResponse.error(code)));
    }

}
