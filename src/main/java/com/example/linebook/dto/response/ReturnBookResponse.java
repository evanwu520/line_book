package com.example.linebook.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReturnBookResponse {
    private Long id;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
}
