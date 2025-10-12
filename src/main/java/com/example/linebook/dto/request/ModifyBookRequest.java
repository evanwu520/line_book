package com.example.linebook.dto.request;

import com.example.linebook.entity.BookType;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class ModifyBookRequest {
    @Positive(message = "quantity must be positive")
    private Long id;
    @NotNull(message = "title cannot be null")
    private String title;
    @NotNull(message = "author cannot be null")
    private String author;
    @Positive(message = "quantity must be positive")
    private int publicationYear;
    @NotNull(message = "bookType cannot be null")
    private BookType type;
}
