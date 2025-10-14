package com.example.linebook.service;

import com.example.linebook.dto.request.AddBookRequest;
import com.example.linebook.dto.request.ModifyBookRequest;
import com.example.linebook.dto.response.AddBookResponse;
import com.example.linebook.dto.response.BookSearchResponse;
import com.example.linebook.dto.response.ModifyBookResponse;
import com.example.linebook.entity.*;
import com.example.linebook.entity.custom.BookSearch;
import com.example.linebook.exception.ApiException;
import com.example.linebook.exception.ErrorCode;
import com.example.linebook.repository.BookCopyRepository;
import com.example.linebook.repository.BookRepository;
import com.example.linebook.repository.LibraryRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public ModifyBookResponse modifyBook(ModifyBookRequest modifyBookRequest) throws ApiException {

        Book book = bookRepository.findById(modifyBookRequest.getId()).orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "BOOK_NOT_FOUND"));
        book.setTitle(modifyBookRequest.getTitle());
        book.setAuthor(modifyBookRequest.getAuthor());
        book.setPublicationYear(modifyBookRequest.getPublicationYear());
        book.setType(modifyBookRequest.getType());
        book = bookRepository.save(book);

        return modelMapper.map(book, ModifyBookResponse.class);
    }

    /**
     * addBook
     * @param addBookRequest
     * @return
     */
    @Transactional
    public AddBookResponse addBook(AddBookRequest addBookRequest) throws ApiException {

        Book book = new Book();
        book.setTitle(addBookRequest.getTitle());
        book.setAuthor(addBookRequest.getAuthor());
        book.setPublicationYear(addBookRequest.getPublicationYear());
        book.setType(addBookRequest.getType());

        book = bookRepository.save(book);

        Library library = libraryRepository.findById(addBookRequest.getLibraryId()).orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "Library not found"));

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
     * @param page
     * @param size
     * @return
     */
    public BookSearchResponse searchBooks(String title, String author, BookType bookType,
                                          int year, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Book> books  = bookRepository.findBooks(title, author, bookType, year, pageable);

        List<BookSearch> bookSearches = books.getContent().stream().distinct().map(book -> {
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

        bookSearchResponse.setTotalPages(books.getTotalPages());
        bookSearchResponse.setTotalCount(books.getTotalElements());
        bookSearchResponse.setBooks(bookResponseList);

        return bookSearchResponse;
    }
}
