package com.example.linebook.repository;

import com.example.linebook.entity.Loan;
import com.example.linebook.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    long countByUserAndReturnDateIsNull(User user);
    List<Loan> findByDueDateBeforeAndReturnDateIsNull(LocalDate date);
}
