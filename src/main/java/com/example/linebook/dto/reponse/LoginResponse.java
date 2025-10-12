package com.example.linebook.dto.reponse;

import com.example.linebook.entity.Role;
import lombok.Data;

import java.util.Set;

@Data
public class LoginResponse {

    private Long userId;
    private String username;
    private String token;
    private Set<Role> roles;
}
