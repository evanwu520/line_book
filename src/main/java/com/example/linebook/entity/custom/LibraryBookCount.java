package com.example.linebook.entity.custom;


import com.example.linebook.entity.BookCopyStatus;
import com.example.linebook.entity.Library;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LibraryBookCount {
    private Library library;
    private BookCopyStatus status;
    private Long count;
}
