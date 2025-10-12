package com.example.linebook.entity;

import javax.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "book_copy_id")
    private BookCopy bookCopy;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDate loanDate;

    private LocalDate dueDate;

    private LocalDate returnDate;
}
