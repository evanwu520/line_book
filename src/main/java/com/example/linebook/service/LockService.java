package com.example.linebook.service;

public interface LockService {

    boolean tryBookLockById(Long bookId);

    void unlockBookId(Long bookId);

    boolean tryBookCopyLockById(Long bookCopyId);

    void unlockBookCopyId(Long bookCopyId);
}
