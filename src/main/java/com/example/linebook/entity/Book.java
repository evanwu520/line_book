package com.example.linebook.entity;

import javax.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(
        name = "book",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"title", "author", "publication_year", "type"})
        }
)
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String author;

    @Column(name = "publication_year")
    private int publicationYear;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private BookType type;

}
