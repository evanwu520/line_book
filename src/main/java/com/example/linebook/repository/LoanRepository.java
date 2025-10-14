package com.example.linebook.repository;

import com.example.linebook.entity.*;
import com.example.linebook.entity.custom.LibraryBookCount;
import com.example.linebook.entity.custom.LoanBookCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    long countByUserAndReturnDateIsNull(User user);
    List<Loan> findByDueDateBeforeAndReturnDateIsNull(LocalDate date);

    @Query("SELECT NEW com.example.linebook.entity.custom.LoanBookCount(b.type, COUNT(l)) " +
            "FROM Loan l JOIN l.bookCopy bc JOIN bc.book b " +
            "WHERE l.user = :user  AND l.returnDate IS NULL GROUP BY b.type")
    List<LoanBookCount> countLoanBookGroupByBookTypeByUser(@Param("user") User user);

}
