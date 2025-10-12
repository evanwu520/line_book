package com.example.linebook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LineBookApplication {

    public static void main(String[] args) {
        SpringApplication.run(LineBookApplication.class, args);
    }

}
