package com.example.linebook.controller;

import com.example.linebook.dto.request.AddBookRequest;
import com.example.linebook.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    BookService bookService;
    
    @PostMapping
    @PreAuthorize("hasAuthority('MANAGE_BOOKS')")
    public ResponseEntity<?> addBook(@RequestBody AddBookRequest addBookRequest) {
        return ResponseEntity.ok(bookService.addBook(addBookRequest));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('BORROW_BOOKS')")
    public ResponseEntity<?> searchBooks(@RequestHeader("Authorization") String token ,  @RequestParam String query) {
        return ResponseEntity.ok(bookService.searchBooks(query));
    }
}
