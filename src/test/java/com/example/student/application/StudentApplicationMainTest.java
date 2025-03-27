package com.example.student.application;

import com.example.student.StudentApplication;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import static org.mockito.Mockito.*;

class StudentApplicationMainTest {

    @Test
    void testMainMethod() {
        // Mock SpringApplication
        try (var mockStatic = Mockito.mockStatic(SpringApplication.class)) {
            // Call the main method
            StudentApplication.main(new String[]{});

            // Verify that SpringApplication.run() was called exactly once
            mockStatic.verify(() -> SpringApplication.run(StudentApplication.class, new String[]{}), times(1));
        }
    }
}
