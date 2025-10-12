package com.example.linebook.service;

import com.example.linebook.dto.request.AddBookRequest;
import com.example.linebook.dto.request.BookSearchResult;
import com.example.linebook.entity.*;
import com.example.linebook.repository.BookCopyRepository;
import com.example.linebook.repository.BookRepository;
import com.example.linebook.repository.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    BookRepository bookRepository;
    @Autowired
    BookCopyRepository bookCopyRepository;
    @Autowired
    LibraryRepository libraryRepository;


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

    public List<BookSearchResult> searchBooks(String query) {
        List<Book> books = new ArrayList<>();
        books.addAll(bookRepository.findByTitleContaining(query));
        books.addAll(bookRepository.findByAuthorContaining(query));

        return books.stream().distinct().map(book -> {
            Map<String, Long> availableCopies = bookCopyRepository.countAvailableCopiesByLibrary(book, BookCopyStatus.AVAILABLE)
                    .stream()
                    .collect(Collectors.toMap(o -> (String) o[0], o -> (Long) o[1]));
            return new BookSearchResult(book, availableCopies);
        }).collect(Collectors.toList());
    }
}
