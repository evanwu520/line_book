package com.example.linebook.service;

import com.example.linebook.dto.request.ReturnRequest;
import com.example.linebook.entity.*;
import com.example.linebook.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Slf4j
@Service
public class LoanService {


    @Autowired
    LoanRepository loanRepository;
    @Autowired
    BookCopyRepository bookCopyRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    LibraryRepository libraryRepository;

    @Value("${book.loan.limit}")
    private int bookLoanLimit;

    @Transactional
    public Loan borrowBook(Long userId, Long bookId, Long libraryId) throws Exception {

        User user = userRepository.findById(userId).orElseThrow(() -> new Exception("USER_NOT_FOUND"));

        long currentLoans = loanRepository.countByUserAndReturnDateIsNull(user);
        if (currentLoans >= bookLoanLimit) { // Assuming 10 is the max for all types for now
            throw new Exception("LOAN_MAX_LIMIT");
        }

        Library library = libraryRepository.findById(libraryId).orElseThrow(() -> new Exception("Library_NOT_FOUND"));

        List<BookCopy> availableCopies = bookCopyRepository.findByBookAndStatusAndLibrary(new Book() {{ setId(bookId); }},
                BookCopyStatus.AVAILABLE, library);

        if (availableCopies.isEmpty()) {
            throw new Exception("NO_AVAILABLE_BOOK");
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

    @Transactional
    public Loan returnBook(ReturnRequest returnRequest) {
        Loan loan = loanRepository.findById(returnRequest.getLoanId()).orElseThrow(() -> new RuntimeException("Loan not found"));
        loan.setReturnDate(LocalDate.now());

        BookCopy bookCopy = loan.getBookCopy();
        bookCopy.setStatus(BookCopyStatus.AVAILABLE);
        bookCopyRepository.save(bookCopy);

        return loanRepository.save(loan);
    }
}
