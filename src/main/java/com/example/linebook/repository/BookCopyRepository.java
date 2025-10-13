package com.example.linebook.repository;

import com.example.linebook.entity.Book;
import com.example.linebook.entity.BookCopy;
import com.example.linebook.entity.BookCopyStatus;
import com.example.linebook.entity.Library;
import com.example.linebook.entity.custom.LibraryBookCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface BookCopyRepository extends JpaRepository<BookCopy, Long> {

//    @Query("SELECT b.library.id, b.library.name, count(b) FROM BookCopy b WHERE b.book = ?1 AND b.status = ?2 GROUP BY b.library.name")
//    List<Object[]> countAvailableCopiesByLibrary(Book book, BookCopyStatus status);

    @Query("SELECT NEW com.example.linebook.entity.custom.LibraryBookCount(bc.library, COUNT(bc.id)) " +
            "FROM BookCopy bc JOIN bc.library l " +
            "WHERE bc.book = :book AND bc.status = :status " +
            "GROUP BY l.name")
    List<LibraryBookCount> countAvailableCopiesByLibrary(@Param("book") Book book,
                                                         @Param("status") BookCopyStatus status);

    List<BookCopy> findByBookAndStatusAndLibrary(Book book, BookCopyStatus status, Library library);
}
