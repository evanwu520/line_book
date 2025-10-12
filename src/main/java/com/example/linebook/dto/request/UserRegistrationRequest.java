package com.example.linebook.dto.request;

import lombok.Data;

@Data
public class UserRegistrationRequest {
    private int userType;
    private String username;
    private String password;
}
