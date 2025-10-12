package com.example.linebook.dto.response;

import com.example.linebook.entity.Book;
import com.example.linebook.entity.BookType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class BookSearchResponse {

    private List<book> books;

    @Data
    public static class book{
        private Long id;
        private String title;
        private String author;
        private int publicationYear;
        private BookType type;
        private Map<String, Long> availableCopies;
    }
}


