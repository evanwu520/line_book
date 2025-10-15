package com.example.linebook.dto.request;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.*;

@Data
public class UserRegistrationRequest {

    @Min(value = 1, message = "userType must be at least 1")
    @Max(value = 2, message = "userType must be at most 2")
    @ApiModelProperty(name = "1:member 2:library",  example = "1")
    private int userType;

    @NotBlank(message = "password cannot be blank")
    private String username;

    @NotBlank(message = "password cannot be blank")

    @Size(min = 8, message = "password must be at least 8 characters long")
    private String password;
}
