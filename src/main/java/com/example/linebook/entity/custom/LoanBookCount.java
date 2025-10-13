package com.example.linebook.entity.custom;


import com.example.linebook.entity.BookType;
import com.example.linebook.entity.Library;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoanBookCount {
    private BookType bookType;
    private Long count;
}
