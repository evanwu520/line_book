package com.example.linebook.repository;

import com.example.linebook.entity.Book;
import com.example.linebook.entity.BookCopy;
import com.example.linebook.entity.BookCopyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface BookCopyRepository extends JpaRepository<BookCopy, Long> {

    @Query("SELECT b.library.name, count(b) FROM BookCopy b WHERE b.book = ?1 AND b.status = ?2 GROUP BY b.library.name")
    List<Object[]> countAvailableCopiesByLibrary(Book book, BookCopyStatus status);

    List<BookCopy> findByBookAndStatus(Book book, BookCopyStatus status);
}
