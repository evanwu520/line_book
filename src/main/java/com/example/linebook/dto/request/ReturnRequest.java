package com.example.linebook.dto.request;

import lombok.Data;

import javax.validation.constraints.Positive;

@Data
public class ReturnRequest {

    @Positive(message = "loanId must be positive")
    private Long loanId;
}
