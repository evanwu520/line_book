package com.example.linebook.controller;

import com.example.linebook.dto.ApiResponse;
import com.example.linebook.dto.request.AddBookRequest;
import com.example.linebook.dto.request.ModifyBookRequest;
import com.example.linebook.dto.response.AddBookResponse;
import com.example.linebook.dto.response.BookSearchResponse;
import com.example.linebook.dto.response.ModifyBookResponse;
import com.example.linebook.entity.Book;
import com.example.linebook.entity.custom.BookSearch;
import com.example.linebook.entity.BookType;
import com.example.linebook.service.BookService;
import com.example.linebook.service.LockServcie;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Slf4j
@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    BookService bookService;

    @Autowired
    LockServcie lockServcie;

    @PutMapping
    @PreAuthorize("hasAuthority('MANAGE_BOOKS')")
    public ResponseEntity<ApiResponse<ModifyBookResponse>> modifyBook(@RequestHeader("Authorization") String token, @RequestBody ModifyBookRequest modifyBookRequest) {

        try {
            if (!lockServcie.tryBookLockById(modifyBookRequest.getId())){
                return ResponseEntity.ok(ApiResponse.error("OPERATION_BUSY"));
            }
            ModifyBookResponse modifyBookResponse = bookService.modifyBook(modifyBookRequest);
            return ResponseEntity.ok(ApiResponse.success(modifyBookResponse));
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return ResponseEntity.ok(ApiResponse.error(ex.getMessage()));
        } finally {
            lockServcie.unlock(modifyBookRequest.getId());
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('MANAGE_BOOKS')")
    public ResponseEntity<ApiResponse<AddBookResponse>> addBook(@RequestHeader("Authorization") String token, @RequestBody AddBookRequest addBookRequest) {

        try {
            AddBookResponse addBookResponse = bookService.addBook(addBookRequest);
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
                                                                       @RequestParam(required = false) Optional<BookType> bookType,
                                                                       @RequestParam(required = false, defaultValue = "0") int year) {

       BookSearchResponse bookSearchResponse = bookService.searchBooks(title, author, bookType.orElse(null), year);
       return ResponseEntity.ok(ApiResponse.success(bookSearchResponse));
    }
}
