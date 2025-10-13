package com.example.linebook.dto.response;

import com.example.linebook.entity.BookCopy;
import com.example.linebook.entity.User;
import lombok.Data;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.time.LocalDate;

@Data
public class BorrowBookResponse {
    private Long id;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
}
