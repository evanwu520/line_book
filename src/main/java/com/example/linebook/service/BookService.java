package com.example.linebook.service;

import com.example.linebook.dto.request.AddBookRequest;
import com.example.linebook.dto.request.ModifyBookRequest;
import com.example.linebook.dto.response.AddBookResponse;
import com.example.linebook.dto.response.BookSearchResponse;
import com.example.linebook.dto.response.ModifyBookResponse;
import com.example.linebook.entity.*;
import com.example.linebook.entity.custom.BookSearch;
import com.example.linebook.repository.BookCopyRepository;
import com.example.linebook.repository.BookRepository;
import com.example.linebook.repository.LibraryRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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

    @Autowired
    private ModelMapper modelMapper;

    /**
     * modifyBook
     * @param modifyBookRequest
     * @return
     * @throws Exception
     */
    public ModifyBookResponse modifyBook(ModifyBookRequest modifyBookRequest) throws Exception {

        Book book = bookRepository.findById(modifyBookRequest.getId()).orElseThrow(() -> new Exception("BOOK_NOT_FOUND"));
        modelMapper.map(modifyBookRequest, book);
        book = bookRepository.save(book);

        return modelMapper.map(book, ModifyBookResponse.class);
    }

    /**
     * addBook
     * @param addBookRequest
     * @return
     */
    @Transactional
    public AddBookResponse addBook(AddBookRequest addBookRequest) {
        Book book = modelMapper.map(addBookRequest, Book.class);
        book = bookRepository.save(book);

        Library library = libraryRepository.findById(addBookRequest.getLibraryId()).orElseThrow(() -> new RuntimeException("Library not found"));

        for (int i = 0; i < addBookRequest.getQuantity(); i++) {
            BookCopy bookCopy = new BookCopy();
            bookCopy.setBook(book);
            bookCopy.setLibrary(library);
            bookCopy.setStatus(BookCopyStatus.AVAILABLE);
            bookCopyRepository.save(bookCopy);
        }

        return modelMapper.map(book, AddBookResponse.class);
    }

    /**
     * searchBooks
     * @param title
     * @param author
     * @param bookType
     * @param year
     * @return
     */
    public BookSearchResponse searchBooks(String title, String author, BookType bookType, int year) {

        List<Book> books  = bookRepository.findBooks(title, author, bookType, year);

        List<BookSearch> bookSearches = books.stream().distinct().map(book -> {
            return new BookSearch(book, bookCopyRepository.countAvailableCopiesByLibrary(book, BookCopyStatus.AVAILABLE));
        }).collect(Collectors.toList());

        BookSearchResponse bookSearchResponse = new BookSearchResponse();
        List<BookSearchResponse.book> bookResponseList =  bookSearches.stream().distinct().map(bookSearch -> {
            BookSearchResponse.book  bookResponse = modelMapper.map(bookSearch.getBook(), BookSearchResponse.book.class);
            bookResponse.setAvailableCopies( bookSearch.getAvailableCopies().stream()
                    .map(lb-> new BookSearchResponse.LibraryBookCount(lb.getLibrary().getId(), lb.getLibrary().getName(),lb.getCount()))
                    .collect(Collectors.toList())
            );
            return bookResponse;
        }).collect(Collectors.toList());

        bookSearchResponse.setBooks(bookResponseList);
        return bookSearchResponse;
    }
}
