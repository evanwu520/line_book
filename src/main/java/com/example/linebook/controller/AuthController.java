package com.example.linebook.controller;

import com.example.linebook.dto.response.RegisterUseResponse;
import com.example.linebook.exception.ApiException;
import com.example.linebook.exception.ErrorCode;
import com.example.linebook.util.JwtTokenUtil;
import com.example.linebook.dto.ApiResponse;
import com.example.linebook.dto.response.LoginResponse;
import com.example.linebook.dto.request.LoginRequest;
import com.example.linebook.dto.request.UserRegistrationRequest;
import com.example.linebook.service.UserService;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {


    @Autowired
    UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 201, message = ""),
            @io.swagger.annotations.ApiResponse(code = 400, message = ""),
            @io.swagger.annotations.ApiResponse(code = 403, message = ""),
            @io.swagger.annotations.ApiResponse(code = 409, message = ""),
            @io.swagger.annotations.ApiResponse(code = 500, message = ""),
    })
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterUseResponse>> registerUser(@Valid @RequestBody UserRegistrationRequest registrationRequest) {

        try {
            RegisterUseResponse registerUseResponse = userService.registerNewUser(registrationRequest);
            return new ResponseEntity<>(ApiResponse.success(registerUseResponse), HttpStatus.CREATED);
        } catch (DataIntegrityViolationException de) {
            log.error(de.getMessage());
            return new ResponseEntity<>(ApiResponse.error(ErrorCode.DUPLICATE_UNIQUE), HttpStatus.CONFLICT);
        } catch (ApiException ex) {
            log.error(ex.getMessage());
            return ResponseEntity.ok(ApiResponse.error(ex.getErrorCode()));
        }
    }

    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 200, message = ""),
            @io.swagger.annotations.ApiResponse(code = 400, message = ""),
            @io.swagger.annotations.ApiResponse(code = 500, message = ""),
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {

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
        } catch (ApiException ex) {
            log.error(ex.getMessage());
            return ResponseEntity.ok(ApiResponse.error(ex.getErrorCode()));
        }
    }
}
