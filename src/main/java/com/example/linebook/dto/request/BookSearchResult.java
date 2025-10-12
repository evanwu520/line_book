package com.example.linebook.dto.request;

import com.example.linebook.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class BookSearchResult {
    private Book book;
    private Map<String, Long> availableCopies;
}
