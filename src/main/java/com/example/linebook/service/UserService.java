package com.example.linebook.service;

import com.example.linebook.dto.request.UserRegistrationRequest;
import com.example.linebook.dto.response.LoginResponse;
import com.example.linebook.dto.response.RegisterUseResponse;
import com.example.linebook.entity.Role;
import com.example.linebook.entity.User;
import com.example.linebook.repository.RoleRepository;
import com.example.linebook.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${user.authenticate.url}")
    private String userAuthenticateUrl;

    /**
     * login
     * @param password
     * @param password
     * @return
     * @throws Exception
     */
    public LoginResponse login(String username, String password) throws  Exception {

        Optional<User> userOptional = userRepository.findByUsername(username);
        userOptional.orElseThrow(() -> new Exception("USER_NOT_FOUND"));

        User user = userOptional.get();

        if (!password.equals(user.getPassword())) {
            throw new Exception("VERIFY_PASSWORD_FAIL");
        }

        return  modelMapper.map(user, LoginResponse.class);
    }

    /**
     * registerNewUser
     * @param registrationRequest
     * @return
     */
    public RegisterUseResponse registerNewUser(UserRegistrationRequest registrationRequest) throws  Exception{

        User newUser = new User();
        newUser.setUsername(registrationRequest.getUsername());
        newUser.setPassword(registrationRequest.getPassword());
        Role memberRole = roleRepository.findByName("MEMBER");
        // LIBRARIAN
        if (registrationRequest.getUserType() == 2) {
            // API call for validation
            if (!authenticate(true, newUser.getUsername())) {
                throw new Exception("VERIFY_FAIL");
            }
             memberRole = roleRepository.findByName("LIBRARIAN");
        }
        newUser.setRoles(new HashSet<>(Collections.singletonList(memberRole)));
        User savedUser = userRepository.save(newUser);
        return modelMapper.map(savedUser, RegisterUseResponse.class);
    }

    /**
     *  API call for validation
     * @param isTest
     * @return
     */
    private boolean authenticate(boolean isTest, String userName) {

        if (isTest) {
            return true;
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", userName);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        // Define the generic type for the Map
        ParameterizedTypeReference<Map<String, String>> responseType = new ParameterizedTypeReference<Map<String, String>>() {};

        ResponseEntity<Map<String, String>> responseEntity =
                restTemplate.exchange(userAuthenticateUrl, HttpMethod.GET,  requestEntity, responseType);

        Map<String, String> responseMap = responseEntity.getBody();

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            // Process the received map
            if (responseMap != null) {
                log.info("Received Map:");
                responseMap.forEach((key, value) -> log.info("{}: {}", key, value));
                return true;
            } else {
                log.error("Response body is null.");
            }
        }

       return false;
    }
}
