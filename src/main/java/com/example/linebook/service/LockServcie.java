package com.example.linebook.service;


import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class LockServcie {

    private static Set<Long> bookKebys= ConcurrentHashMap.newKeySet();
    private static final long TIMEOUT_MS = 3000;
    private static final long RETRY_INTERVAL_MS = 1000;

    public boolean tryBookLockById(Long bookId) {

        try {

            long waited = 0;

            while( waited < TIMEOUT_MS){

                if (bookKebys.add(bookId)){
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

    public void unlock(Long bookId) {
        bookKebys.remove(bookId);
    }
}
