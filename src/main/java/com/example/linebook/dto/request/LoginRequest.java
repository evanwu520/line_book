package com.example.linebook.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LoginRequest {

    @NotNull(message = "username cannot be null")
    private String username;

    @NotNull(message = "password cannot be null")
    private String password;
}
