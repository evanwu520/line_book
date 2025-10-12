package com.example.linebook.controller;

import com.example.linebook.dto.response.RegisterUseResponse;
import com.example.linebook.util.JwtTokenUtil;
import com.example.linebook.dto.ApiResponse;
import com.example.linebook.dto.response.LoginResponse;
import com.example.linebook.dto.request.LoginRequest;
import com.example.linebook.dto.request.UserRegistrationRequest;
import com.example.linebook.entity.User;
import com.example.linebook.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterUseResponse>> registerUser(@RequestBody UserRegistrationRequest registrationRequest) {

        try {
            RegisterUseResponse registerUseResponse = new RegisterUseResponse();
            User user = userService.registerNewUser(registrationRequest);
            registerUseResponse.setUserId(user.getId());
            registerUseResponse.setUsername(user.getUsername());
            registerUseResponse.setRoles(user.getRoles());
            return ResponseEntity.ok(ApiResponse.success(registerUseResponse));
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return ResponseEntity.ok(ApiResponse.error(ex.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest loginRequest) {

        try {
            LoginResponse loginResponse = new LoginResponse();
            User user = userService.login(loginRequest);
            loginResponse.setUserId(user.getId());
            loginResponse.setUsername(user.getUsername());
            loginResponse.setRoles(user.getRoles());
            List<String> permissionList = user.getRoles().stream()
                    .flatMap(r -> r.getPermissions().stream()) // flatten all permissions across roles
                    .map(p -> p.getName())                    // extract permission name
                    .distinct()                               // optional: remove duplicates
                    .collect(Collectors.toList());
            // generate jwt
            loginResponse.setToken(JwtTokenUtil.generateToken(user.getId(), permissionList));
            return ResponseEntity.ok(ApiResponse.success(loginResponse));
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return ResponseEntity.ok(ApiResponse.error(ex.getMessage()));
        }
    }
}
