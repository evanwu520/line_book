package com.example.linebook.dto.response;

import com.example.linebook.entity.Role;
import lombok.Data;

import java.util.Set;

@Data
public class RegisterUseResponse {

    private Long userId;
    private String username;
    private Set<Role> roles;
}
