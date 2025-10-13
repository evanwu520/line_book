package com.example.linebook.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class LockService {

    private static final long TIMEOUT_MS = 3000;
    private static final long RETRY_INTERVAL_MS = 1000;

    // atomic for book modify
    private static Set<Long> bookIds = ConcurrentHashMap.newKeySet();
    private static Set<Long> bookCopyIds= ConcurrentHashMap.newKeySet();

    public boolean tryBookLockById(Long bookId) {
        try {

            long waited = 0;

            while( waited < TIMEOUT_MS){

                if (bookIds.add(bookId)){
                    return true;
                }
                Thread.sleep(RETRY_INTERVAL_MS);
                waited += RETRY_INTERVAL_MS;
            }
        } catch (InterruptedException ie) {
            log.error(ie.getMessage());
            Thread.currentThread().interrupt();
            return false;
        }

        return false;
    }

    public void unlockBookId(Long bookId) {
        bookIds.remove(bookId);
    }


    public boolean tryBookCopyLockById(Long bookCopyId) {

        try {

            long waited = 0;

            while( waited < TIMEOUT_MS){

                if (bookCopyIds.add(bookCopyId)){
                    return true;
                }
                Thread.sleep(RETRY_INTERVAL_MS);
                waited += RETRY_INTERVAL_MS;
            }
        } catch (InterruptedException ie) {
            log.error(ie.getMessage());
            Thread.currentThread().interrupt();
            return false;
        }

        return false;
    }

    public void unlockBookCopyId(Long bookCopyId) {
        bookCopyIds.remove(bookCopyId);
    }
}
