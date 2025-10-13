package com.example.linebook.dto.request;

import lombok.Data;

import javax.validation.constraints.Positive;

@Data
public class BorrowRequest {
    @Positive(message = "bookId must be positive")
    private Long bookId;

    @Positive(message = "libraryId must be positive")
    private Long libraryId;

}
