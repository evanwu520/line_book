package com.example.linebook.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class BookSearch {

    private Book book;
    private Map<String, Long> availableCopies;
}
