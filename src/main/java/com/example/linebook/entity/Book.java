package com.example.linebook.entity;

import javax.persistence.*;
import lombok.Data;

@Data
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String author;

    private int publicationYear;

    @Enumerated(EnumType.STRING)
    private BookType type;
}
