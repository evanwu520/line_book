package com.example.linebook.controller;

import com.example.linebook.dto.ApiResponse;
import com.example.linebook.dto.request.AddBookRequest;
import com.example.linebook.dto.request.ModifyBookRequest;
import com.example.linebook.dto.response.AddBookResponse;
import com.example.linebook.dto.response.BookSearchResponse;
import com.example.linebook.dto.response.ModifyBookResponse;
import com.example.linebook.dto.response.RegisterUseResponse;
import com.example.linebook.entity.Book;
import com.example.linebook.entity.BookSearch;
import com.example.linebook.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    BookService bookService;


    @PutMapping
    @PreAuthorize("hasAuthority('MANAGE_BOOKS')")
    public ResponseEntity<ApiResponse<ModifyBookResponse>> modifyBook(@RequestHeader("Authorization") String token, @RequestBody ModifyBookRequest modifyBookRequest) {

        try {
            ModifyBookResponse modifyBookResponse = new ModifyBookResponse();
            Book book = bookService.modifyBook(modifyBookRequest);
            modifyBookResponse.setId(book.getId());
            modifyBookResponse.setType(book.getType());
            modifyBookResponse.setTitle(book.getTitle());
            modifyBookResponse.setAuthor(book.getAuthor());
            modifyBookResponse.setPublicationYear(book.getPublicationYear());
            return ResponseEntity.ok(ApiResponse.success(modifyBookResponse));
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return ResponseEntity.ok(ApiResponse.error(ex.getMessage()));
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('MANAGE_BOOKS')")
    public ResponseEntity<ApiResponse<AddBookResponse>> addBook(@RequestHeader("Authorization") String token, @RequestBody AddBookRequest addBookRequest) {

        try {
            AddBookResponse addBookResponse = new AddBookResponse();
            Book book = bookService.addBook(addBookRequest);
            addBookResponse.setId(book.getId());
            addBookResponse.setType(book.getType());
            addBookResponse.setTitle(book.getTitle());
            addBookResponse.setAuthor(book.getAuthor());
            addBookResponse.setPublicationYear(book.getPublicationYear());
            return ResponseEntity.ok(ApiResponse.success(addBookResponse));
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return ResponseEntity.ok(ApiResponse.error(ex.getMessage()));
        }
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('BORROW_BOOKS','MANAGE_BOOKS')")
    public ResponseEntity<ApiResponse<BookSearchResponse>> searchBooks(@RequestHeader("Authorization") String token,
                                                                       @RequestParam(required = false) String title,
                                                                       @RequestParam(required = false) String author,
                                                                       @RequestParam(required = false, defaultValue = "0") int year) {

       List<BookSearch> books = bookService.searchBooks(title, author, year);
       BookSearchResponse bookSearchResponse = new BookSearchResponse();
       List<BookSearchResponse.book> bookResponseList =  books.stream().distinct().map(book -> {
            BookSearchResponse.book  bookResponse = new BookSearchResponse.book();
            bookResponse.setId(book.getBook().getId());
            bookResponse.setType(book.getBook().getType());
            bookResponse.setTitle(book.getBook().getTitle());
            bookResponse.setAuthor(book.getBook().getAuthor());
            bookResponse.setPublicationYear(book.getBook().getPublicationYear());
            bookResponse.setAvailableCopies(book.getAvailableCopies());
            return bookResponse;
        }).collect(Collectors.toList());

        bookSearchResponse.setBooks(bookResponseList);

        return ResponseEntity.ok(ApiResponse.success(bookSearchResponse));
    }
}
