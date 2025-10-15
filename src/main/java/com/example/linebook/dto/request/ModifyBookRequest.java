package com.example.linebook.dto.request;

import com.example.linebook.entity.BookType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class ModifyBookRequest {
    @Positive(message = "quantity must be positive")
    private Long id;
    @NotBlank(message = "title cannot be blank")
    private String title;
    @NotBlank(message = "author cannot be blank")
    private String author;
    @Positive(message = "quantity must be positive")
    private int publicationYear;
    @NotBlank(message = "bookType cannot be blank")
    private BookType type;
}
