package com.example.linebook.controller;

import com.example.linebook.dto.reponse.RegisterUseResponse;
import com.example.linebook.util.JwtTokenUtil;
import com.example.linebook.dto.ApiResponse;
import com.example.linebook.dto.reponse.LoginResponse;
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
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationRequest registrationRequest) {

        RegisterUseResponse registerUseResponse = new RegisterUseResponse();

        try {
            User user = userService.registerNewUser(registrationRequest);
            registerUseResponse.setUserId(user.getId());
            registerUseResponse.setUsername(user.getUsername());
            registerUseResponse.setRoles(user.getRoles());

        } catch (Exception ex) {
            log.error(ex.getMessage());
            return ResponseEntity.ok(ApiResponse.error("REGISTER_USER_FAIL"));
        }

        return ResponseEntity.ok(ApiResponse.success(registerUseResponse));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest loginRequest) {

        LoginResponse loginResponse = new LoginResponse();

        try {
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
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return ResponseEntity.ok(ApiResponse.error("LOGIN_FAIL"));
        }

        return ResponseEntity.ok(ApiResponse.success(loginResponse));
    }
}
