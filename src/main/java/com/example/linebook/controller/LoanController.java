package com.example.linebook.controller;

import com.example.linebook.dto.ApiResponse;
import com.example.linebook.dto.request.BorrowRequest;
import com.example.linebook.dto.request.ReturnRequest;

import com.example.linebook.dto.response.BorrowBookResponse;
import com.example.linebook.dto.response.ReturnBookResponse;
import com.example.linebook.service.LoanService;
import com.example.linebook.service.LockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RestController
@RequestMapping("/api/loans")
public class LoanController {

    @Autowired
    LoanService loanService;

    @Autowired
    LockService lockService;

    @PostMapping("/borrow")
    @PreAuthorize("hasAuthority('BORROW_BOOKS')")
    public ResponseEntity<ApiResponse<BorrowBookResponse>> borrowBook(@ApiIgnore @RequestAttribute("userId") Long userId,
                                                                      @RequestHeader("Authorization") String token,
                                                                      @RequestBody BorrowRequest borrowRequest) {
        try {
           return ResponseEntity.ok( ApiResponse.success(loanService.borrowBook(userId, borrowRequest.getBookId(), borrowRequest.getLibraryId())));
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return ResponseEntity.ok(ApiResponse.error(ex.getMessage()));
        }
    }

    @PostMapping("/return")
    @PreAuthorize("hasAuthority('BORROW_BOOKS')")
    public ResponseEntity<ApiResponse<ReturnBookResponse>> returnBook(@ApiIgnore @RequestAttribute("userId") Long userId,
                                                         @RequestHeader("Authorization") String token,
                                                         @RequestBody ReturnRequest returnRequest) {

        try {
            return ResponseEntity.ok(ApiResponse.success(loanService.returnBook(returnRequest)));
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return ResponseEntity.ok(ApiResponse.error(ex.getMessage()));
        }
    }
}
