package com.example.linebook.service;

import com.example.linebook.dto.request.ReturnRequest;
import com.example.linebook.dto.response.BorrowBookResponse;
import com.example.linebook.dto.response.ReturnBookResponse;
import com.example.linebook.exception.ApiException;

public interface LoanService {

    BorrowBookResponse borrowBook(Long userId, Long bookId, Long libraryId) throws ApiException;

    ReturnBookResponse returnBook(ReturnRequest returnRequest) throws ApiException;
}
