package com.example.linebook.dto.request;

import lombok.Data;

import javax.validation.constraints.Positive;

@Data
public class BorrowRequest {
    @Positive(message = "quantity must be positive")
    private Long bookId;

}
