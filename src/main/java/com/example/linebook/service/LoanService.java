package com.example.linebook.service;

import com.example.linebook.dto.request.ReturnRequest;
import com.example.linebook.dto.response.BorrowBookResponse;
import com.example.linebook.dto.response.ReturnBookResponse;
import com.example.linebook.entity.*;
import com.example.linebook.entity.custom.LoanBookCount;
import com.example.linebook.exception.ApiException;
import com.example.linebook.exception.ErrorCode;
import com.example.linebook.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
    @Autowired
    LockService lockService;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${book.loan.book.limit}")
    private int bookLoanBookLimit;

    @Value("${book.loan.magazine.limit}")
    private int bookLoanMagazineLimit;

    @Transactional
    public BorrowBookResponse borrowBook(Long userId, Long bookId, Long libraryId) throws ApiException {

        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "USER_NOT_FOUND"));

        List<LoanBookCount> loanBookCount = loanRepository.countLoanBookGroupByBookTypeByUser(user);

//        Map<BookType, Long> bookTypeCountMap = loanBookCount.stream().collect(
//                Collectors.toMap(LoanBookCount :: getBookType,  Function.identity() ));

        Map<BookType, Long> bookTypeCountMap = loanBookCount.stream().collect(
                Collectors.toMap(LoanBookCount::getBookType, LoanBookCount::getCount)
        );

        if (bookTypeCountMap.getOrDefault(BookType.BOOK,0L) >= bookLoanBookLimit) {
            throw new ApiException(ErrorCode.LIMIT_MAX ,"LOAN_BOOK_MAX_LIMIT");
        }

        if (bookTypeCountMap.getOrDefault(BookType.MAGAZINE,0L) >= bookLoanMagazineLimit) {
            throw new ApiException(ErrorCode.LIMIT_MAX ,"LOAN_MAGAZINE_MAX_LIMIT");
        }


        Library library = libraryRepository.findById(libraryId).orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "Library_NOT_FOUND"));

        List<BookCopy> availableCopies = bookCopyRepository.findByBookAndStatusAndLibrary(new Book() {{ setId(bookId); }},
                BookCopyStatus.AVAILABLE, library);

        if (availableCopies.isEmpty()) {
            throw new ApiException(ErrorCode.NOT_FOUND, "NO_AVAILABLE_BOOK");
        }

        BookCopy copyToLoan = null;

        try {

            // get lock success BookCopy
            for (BookCopy bookCopy : availableCopies) {
                if (lockService.tryBookCopyLockById(bookCopy.getId())) {
                    copyToLoan = bookCopy;
                    break;
                }
            }

            if (copyToLoan == null) {
                throw new ApiException(ErrorCode.NOT_FOUND, "NO_AVAILABLE_BOOK");
            }

            copyToLoan.setStatus(BookCopyStatus.LOANED);
            bookCopyRepository.save(copyToLoan);

            Loan loan = new Loan();
            loan.setBookCopy(copyToLoan);
            loan.setUser(user);
            loan.setLoanDate(LocalDate.now());
            loan.setDueDate(LocalDate.now().plusMonths(1));

            return  modelMapper.map(loanRepository.save(loan), BorrowBookResponse.class);
        } finally {
            if (copyToLoan != null) {
                lockService.unlockBookCopyId(copyToLoan.getId());
            };
        }

    }

    @Transactional
    public ReturnBookResponse returnBook(ReturnRequest returnRequest) throws ApiException {
        Loan loan = loanRepository.findById(returnRequest.getLoanId()).orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "LOAN_NOT_FOUND"));

        if ( loan.getReturnDate() != null  ||
                loan.getBookCopy().getStatus() == BookCopyStatus.AVAILABLE) {
            throw new ApiException(ErrorCode.NOT_FOUND, "LOAN_NOT_FOUND");
        }

        loan.setReturnDate(LocalDate.now());

        BookCopy bookCopy = loan.getBookCopy();
        bookCopy.setStatus(BookCopyStatus.AVAILABLE);
        bookCopyRepository.save(bookCopy);

        return modelMapper.map(loanRepository.save(loan), ReturnBookResponse.class);
    }
}
