package com.example.student;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching  // Enable caching in the application
public class StudentApplication {
    public static void main(String[] args) {

        SpringApplication.run(StudentApplication.class, args);
    }
}
