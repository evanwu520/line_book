package com.example.linebook.entity;

import javax.persistence.*;
import lombok.Data;

@Data
@Entity
public class BookCopy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "library_id")
    private Library library;

    @Enumerated(EnumType.STRING)
    private BookCopyStatus status;
}
