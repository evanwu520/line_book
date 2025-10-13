package com.example.linebook.service;

import com.example.linebook.dto.request.AddBookRequest;
import com.example.linebook.dto.request.ModifyBookRequest;
import com.example.linebook.entity.*;
import com.example.linebook.entity.custom.BookSearch;
import com.example.linebook.repository.BookCopyRepository;
import com.example.linebook.repository.BookRepository;
import com.example.linebook.repository.LibraryRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookService {

    @Autowired
    BookRepository bookRepository;
    @Autowired
    BookCopyRepository bookCopyRepository;
    @Autowired
    LibraryRepository libraryRepository;

    /**
     * modifyBook
     * @param modifyBookRequest
     * @return
     * @throws Exception
     */
    public Book modifyBook(ModifyBookRequest modifyBookRequest) throws Exception {

        Book book = bookRepository.findById(modifyBookRequest.getId()).orElseThrow(() -> new Exception("BOOK_NOT_FOUND"));
        book.setTitle(modifyBookRequest.getTitle());
        book.setAuthor(modifyBookRequest.getAuthor());
        book.setPublicationYear(modifyBookRequest.getPublicationYear());
        book.setType(modifyBookRequest.getType());
        book = bookRepository.save(book);

        return book;
    }

    /**
     * addBook
     * @param addBookRequest
     * @return
     */
    @Transactional
    public Book addBook(AddBookRequest addBookRequest) {
        Book book = new Book();
        book.setTitle(addBookRequest.getTitle());
        book.setAuthor(addBookRequest.getAuthor());
        book.setPublicationYear(addBookRequest.getPublicationYear());
        book.setType(addBookRequest.getType());
        book = bookRepository.save(book);

        Library library = libraryRepository.findById(addBookRequest.getLibraryId()).orElseThrow(() -> new RuntimeException("Library not found"));

        for (int i = 0; i < addBookRequest.getQuantity(); i++) {
            BookCopy bookCopy = new BookCopy();
            bookCopy.setBook(book);
            bookCopy.setLibrary(library);
            bookCopy.setStatus(BookCopyStatus.AVAILABLE);
            bookCopyRepository.save(bookCopy);
        }

        return book;
    }

    /**
     * searchBooks
     * @param title
     * @param author
     * @param bookType
     * @param year
     * @return
     */
    public List<BookSearch> searchBooks(String title, String author, BookType bookType, int year) {

        List<Book> books  = bookRepository.findBooks(title, author, bookType, year);

        return books.stream().distinct().map(book -> {
            return new BookSearch(book, bookCopyRepository.countAvailableCopiesByLibrary(book, BookCopyStatus.AVAILABLE));
        }).collect(Collectors.toList());
    }
}
