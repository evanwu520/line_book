package com.example.linebook.controller;

import com.example.linebook.dto.ApiResponse;
import com.example.linebook.dto.request.AddBookRequest;
import com.example.linebook.dto.request.ModifyBookRequest;
import com.example.linebook.dto.response.AddBookResponse;
import com.example.linebook.dto.response.BookSearchResponse;
import com.example.linebook.dto.response.ModifyBookResponse;
import com.example.linebook.entity.BookType;
import com.example.linebook.exception.ApiException;
import com.example.linebook.exception.ErrorCode;
import com.example.linebook.service.BookService;
import com.example.linebook.service.LockService;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    BookService bookService;

    @Autowired
    LockService lockService;


    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 200, message = ""),
            @io.swagger.annotations.ApiResponse(code = 400, message = ""),
            @io.swagger.annotations.ApiResponse(code = 401, message = ""),
            @io.swagger.annotations.ApiResponse(code = 403, message = ""),
            @io.swagger.annotations.ApiResponse(code = 409, message = ""),
            @io.swagger.annotations.ApiResponse(code = 500, message = ""),
    })
    @PutMapping
    @PreAuthorize("hasAuthority('MANAGE_BOOKS')")
    public ResponseEntity<ApiResponse<ModifyBookResponse>> modifyBook(@Valid @RequestHeader("Authorization") String token, @RequestBody ModifyBookRequest modifyBookRequest) {

        boolean isGetBookLockId = false;
        try {

            isGetBookLockId = lockService.tryBookLockById(modifyBookRequest.getId());

            if (!isGetBookLockId) {
                throw new ApiException(ErrorCode.OPERATION_BUSY, "OPERATION_BUSY");
            }
            ModifyBookResponse modifyBookResponse = bookService.modifyBook(modifyBookRequest);
            return new ResponseEntity<>(ApiResponse.success(modifyBookResponse), HttpStatus.CREATED);
        } catch (ApiException ex) {
            log.error(ex.getMessage());
            return new ResponseEntity<>(ApiResponse.error(ErrorCode.DUPLICATE_UNIQUE), HttpStatus.CONFLICT);
        } finally {
            if (isGetBookLockId) {
                lockService.unlockBookId(modifyBookRequest.getId());
            }
        }
    }
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 201, message = ""),
            @io.swagger.annotations.ApiResponse(code = 400, message = ""),
            @io.swagger.annotations.ApiResponse(code = 401, message = ""),
            @io.swagger.annotations.ApiResponse(code = 403, message = ""),
            @io.swagger.annotations.ApiResponse(code = 409, message = ""),
            @io.swagger.annotations.ApiResponse(code = 500, message = ""),
    })
    @PostMapping
    @PreAuthorize("hasAuthority('MANAGE_BOOKS')")
    public ResponseEntity<ApiResponse<AddBookResponse>> addBook(@Valid @RequestHeader("Authorization") String token, @RequestBody AddBookRequest addBookRequest) {

        try {
            AddBookResponse addBookResponse = bookService.addBook(addBookRequest);
            return new ResponseEntity<>(ApiResponse.success(addBookResponse), HttpStatus.CREATED);
        } catch (DataIntegrityViolationException de) {
            log.error(de.getMessage());
            return new ResponseEntity<>(ApiResponse.error(ErrorCode.DUPLICATE_UNIQUE), HttpStatus.CONFLICT);
        }  catch (ApiException ae) {
            log.error(ae.getMessage());
            return ResponseEntity.ok(ApiResponse.error(ae.getErrorCode()));
        }
    }

    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 200, message = ""),
            @io.swagger.annotations.ApiResponse(code = 400, message = ""),
            @io.swagger.annotations.ApiResponse(code = 401, message = ""),
            @io.swagger.annotations.ApiResponse(code = 403, message = ""),
            @io.swagger.annotations.ApiResponse(code = 500, message = ""),
    })
    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('BORROW_BOOKS','MANAGE_BOOKS')")
    public ResponseEntity<ApiResponse<BookSearchResponse>> searchBooks(@RequestHeader("Authorization") String token,
                                                                       @RequestParam(required = false) String title,
                                                                       @RequestParam(required = false) String author,
                                                                       @RequestParam(required = false) Optional<BookType> bookType,
                                                                       @RequestParam(required = false, defaultValue = "0") int year,
                                                                       @RequestParam(required = false, defaultValue = "1") int page,
                                                                       @RequestParam(required = false, defaultValue = "50") int size) {

       BookSearchResponse bookSearchResponse = bookService.searchBooks(title, author, bookType.orElse(null), year, year, size);
       return ResponseEntity.ok(ApiResponse.success(bookSearchResponse));
    }
}
