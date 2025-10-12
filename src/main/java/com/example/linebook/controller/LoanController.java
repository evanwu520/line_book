package com.example.linebook.controller;

import com.example.linebook.dto.ApiResponse;
import com.example.linebook.dto.request.BorrowRequest;
import com.example.linebook.dto.request.ReturnRequest;

import com.example.linebook.service.LoanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/loans")
public class LoanController {


    @Autowired
    LoanService loanService;

    @PostMapping("/borrow")
    @PreAuthorize("hasAuthority('BORROW_BOOKS')")
    public ResponseEntity<?> borrowBook(@RequestAttribute("userId") Long userId,
                                        @RequestHeader("Authorization") String token,
                                        @RequestBody BorrowRequest borrowRequest) {
        try {

           return ResponseEntity.ok( ApiResponse.success(loanService.borrowBook(userId, borrowRequest.getBookId())));

        } catch (Exception ex) {
            log.error(ex.getMessage());
            return ResponseEntity.ok(ApiResponse.error("REGISTER_USER_FAIL"));
        }

    }

    @PostMapping("/return")
    @PreAuthorize("hasAuthority('BORROW_BOOKS')")
    public ResponseEntity<?> returnBook(@RequestAttribute("userId") Long userId,
                                        @RequestHeader("Authorization") String token,
                                        @RequestBody ReturnRequest returnRequest) {
        return ResponseEntity.ok(loanService.returnBook(returnRequest));
    }
}
