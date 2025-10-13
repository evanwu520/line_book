package com.example.linebook.repository;

import com.example.linebook.entity.Book;
import com.example.linebook.entity.BookType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleContaining(String title);

    List<Book> findByAuthorContaining(String author);

    List<Book> findByPublicationYear(int publicationYear);

    @Query("SELECT b FROM Book b " +
            "WHERE (:title IS NULL OR b.title LIKE %:title%) " +
            "AND (:author IS NULL OR b.author LIKE %:author%) " +
            "AND (:type IS NULL OR b.type = :type) " +
            "AND (:year = 0 OR b.publicationYear = :year)")
    Page<Book> findBooks(@Param("title") String title,
                               @Param("author") String author,
                               @Param("type") BookType type,
                               @Param("year") int year,
                               Pageable pageable);

}