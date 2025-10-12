package com.example.linebook.dto.request;

import com.example.linebook.entity.BookType;
import lombok.Data;

@Data
public class AddBookRequest {
    private String title;
    private String author;
    private int publicationYear;
    private BookType type;
    private Long libraryId;
    private int quantity;
}
