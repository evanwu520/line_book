package com.example.linebook.controller;

import com.example.linebook.dto.request.BorrowRequest;
import com.example.linebook.dto.request.ReturnRequest;
import com.example.linebook.service.LoanService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/borrow")
    @PreAuthorize("hasAuthority('BORROW_BOOKS')")
    public ResponseEntity<?> borrowBook(@RequestBody BorrowRequest borrowRequest) {
        return ResponseEntity.ok(loanService.borrowBook(borrowRequest));
    }

    @PostMapping("/return")
    @PreAuthorize("hasAuthority('BORROW_BOOKS')")
    public ResponseEntity<?> returnBook(@RequestBody ReturnRequest returnRequest) {
        return ResponseEntity.ok(loanService.returnBook(returnRequest));
    }
}
