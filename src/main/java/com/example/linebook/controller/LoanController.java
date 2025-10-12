package com.example.linebook.controller;

import com.example.linebook.dto.ApiResponse;
import com.example.linebook.dto.request.BorrowRequest;
import com.example.linebook.dto.request.ReturnRequest;

import com.example.linebook.service.LoanService;
import com.example.linebook.service.LockServcie;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private static final Logger log = LoggerFactory.getLogger(LoanController.class);

    @Autowired
    LoanService loanService;

    @Autowired
    LockServcie lockServcie;

    @PostMapping("/borrow")
    @PreAuthorize("hasAuthority('BORROW_BOOKS')")
    public ResponseEntity<?> borrowBook(@RequestAttribute("userId") Long userId,
                                        @RequestHeader("Authorization") String token,
                                        @RequestBody BorrowRequest borrowRequest) {
        try {
            if (!lockServcie.tryBookLockById(borrowRequest.getBookId())){
                return ResponseEntity.ok(ApiResponse.error("OPERATION_BUSY"));
            }
           return ResponseEntity.ok( ApiResponse.success(loanService.borrowBook(userId, borrowRequest.getBookId())));

        } catch (Exception ex) {
            log.error(ex.getMessage());
            return ResponseEntity.ok(ApiResponse.error("REGISTER_USER_FAIL"));
        } finally {
            lockServcie.unlock(borrowRequest.getBookId());
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
