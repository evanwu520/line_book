package com.example.linebook.service;

import com.example.linebook.dto.request.AddBookRequest;
import com.example.linebook.dto.request.ModifyBookRequest;
import com.example.linebook.dto.response.AddBookResponse;
import com.example.linebook.dto.response.BookSearchResponse;
import com.example.linebook.dto.response.ModifyBookResponse;
import com.example.linebook.entity.BookType;
import com.example.linebook.exception.ApiException;

public interface BookService {

    ModifyBookResponse modifyBook(ModifyBookRequest modifyBookRequest) throws ApiException;

    AddBookResponse addBook(AddBookRequest addBookRequest) throws ApiException;

    BookSearchResponse searchBooks(String title, String author, BookType bookType,
                                   int year, int page, int size);
}
