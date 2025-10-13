package com.example.linebook.controller;

import com.example.linebook.dto.response.RegisterUseResponse;
import com.example.linebook.util.JwtTokenUtil;
import com.example.linebook.dto.ApiResponse;
import com.example.linebook.dto.response.LoginResponse;
import com.example.linebook.dto.request.LoginRequest;
import com.example.linebook.dto.request.UserRegistrationRequest;
import com.example.linebook.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
            RegisterUseResponse registerUseResponse = userService.registerNewUser(registrationRequest);
            return ResponseEntity.ok(ApiResponse.success(registerUseResponse));
        } catch (DataIntegrityViolationException de) {
            log.error(de.getMessage());
            return ResponseEntity.ok(ApiResponse.error("DUPLICATE_UNIQUE"));

        } catch (Exception ex) {
            log.error(ex.getMessage());
            return ResponseEntity.ok(ApiResponse.error(ex.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest loginRequest) {

        try {
            LoginResponse loginResponse  = userService.login(loginRequest.getUsername(), loginRequest.getPassword());

            List<String> permissionList = loginResponse.getRoles().stream()
                    .flatMap(r -> r.getPermissions().stream()) // flatten all permissions across roles
                    .map(p -> p.getName())                    // extract permission name
                    .distinct()                               // optional: remove duplicates
                    .collect(Collectors.toList());
            // generate jwt
            loginResponse.setToken(JwtTokenUtil.generateToken(loginResponse.getUserId(), permissionList));
            return ResponseEntity.ok(ApiResponse.success(loginResponse));
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return ResponseEntity.ok(ApiResponse.error(ex.getMessage()));
        }
    }
}
