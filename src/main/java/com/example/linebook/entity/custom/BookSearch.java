package com.example.linebook.entity.custom;

import com.example.linebook.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BookSearch {

    private Book book;
    private List<LibraryBookCount> bookCopies;

}
