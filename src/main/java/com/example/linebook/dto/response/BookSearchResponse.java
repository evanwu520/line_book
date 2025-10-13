package com.example.linebook.dto.response;

import com.example.linebook.entity.BookType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

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
        private List<LibraryBookCount> availableCopies;
    }
    @Data
    @AllArgsConstructor
    public static class LibraryBookCount {
        private Long libraryId;
        private String name;
        private Long count;
    }
}


