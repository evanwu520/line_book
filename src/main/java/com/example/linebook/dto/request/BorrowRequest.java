package com.example.linebook.dto.request;

import lombok.Data;

@Data
public class BorrowRequest {
    private Long bookId;
    private Long userId;
}
