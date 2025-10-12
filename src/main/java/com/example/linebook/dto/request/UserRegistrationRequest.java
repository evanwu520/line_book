package com.example.linebook.dto.request;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class UserRegistrationRequest {

    @Min(value = 1, message = "userType must be at least 1")
    @Max(value = 2, message = "userType must be at most 2")
    private int userType;

    @NotNull(message = "password cannot be null")
    private String username;
    @NotNull(message = "password cannot be null")
    @Size(min = 8, message = "password must be at least 8 characters long")
    private String password;
}
