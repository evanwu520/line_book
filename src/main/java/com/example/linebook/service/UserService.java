package com.example.linebook.service;

import com.example.linebook.dto.request.UserRegistrationRequest;
import com.example.linebook.dto.response.LoginResponse;
import com.example.linebook.dto.response.RegisterUseResponse;
import com.example.linebook.exception.ApiException;

public interface UserService {

    LoginResponse login(String username, String password) throws ApiException;

    RegisterUseResponse registerNewUser(UserRegistrationRequest registrationRequest) throws ApiException;
}
