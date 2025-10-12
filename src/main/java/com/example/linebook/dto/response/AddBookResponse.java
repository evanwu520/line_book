package com.example.linebook.dto.response;

import com.example.linebook.entity.BookType;
import lombok.Data;


@Data
public class AddBookResponse {

    private Long id;
    private String title;
    private String author;
    private int publicationYear;
    private BookType type;
}
