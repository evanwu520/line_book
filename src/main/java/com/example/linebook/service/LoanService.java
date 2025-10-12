package com.example.linebook.service;

import com.example.linebook.dto.request.BorrowRequest;
import com.example.linebook.dto.request.ReturnRequest;
import com.example.linebook.entity.*;
import com.example.linebook.repository.BookCopyRepository;
import com.example.linebook.repository.LoanRepository;
import com.example.linebook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LoanService {

    @Autowired
    LoanRepository loanRepository;
    @Autowired
    BookCopyRepository bookCopyRepository;
    @Autowired
    UserRepository userRepository;


    public Loan borrowBook(BorrowRequest borrowRequest) {
        User user = userRepository.findById(borrowRequest.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));

        long currentLoans = loanRepository.countByUserAndReturnDateIsNull(user);
        if (currentLoans >= 10) { // Assuming 10 is the max for all types for now
            throw new RuntimeException("User has reached the maximum number of loans.");
        }

        List<BookCopy> availableCopies = bookCopyRepository.findByBookAndStatus(new Book() {{ setId(borrowRequest.getBookId()); }}, BookCopyStatus.AVAILABLE);
        if (availableCopies.isEmpty()) {
            throw new RuntimeException("No available copies of the book.");
        }

        BookCopy copyToLoan = availableCopies.get(0);
        copyToLoan.setStatus(BookCopyStatus.LOANED);
        bookCopyRepository.save(copyToLoan);

        Loan loan = new Loan();
        loan.setBookCopy(copyToLoan);
        loan.setUser(user);
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusMonths(1));
        return loanRepository.save(loan);
    }

    public Loan returnBook(ReturnRequest returnRequest) {
        Loan loan = loanRepository.findById(returnRequest.getLoanId()).orElseThrow(() -> new RuntimeException("Loan not found"));
        loan.setReturnDate(LocalDate.now());

        BookCopy bookCopy = loan.getBookCopy();
        bookCopy.setStatus(BookCopyStatus.AVAILABLE);
        bookCopyRepository.save(bookCopy);

        return loanRepository.save(loan);
    }
}
