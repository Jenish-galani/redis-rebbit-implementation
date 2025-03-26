package com.example.student.services;

import com.example.student.entities.Student;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class RedisServiceTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private RedisService redisService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOperations); // ✅ Allows unused stubbing
    }

    @Test
    void testSet() throws Exception {
        Student student = new Student(1L, "John Doe", "123 Street");
        String studentJson = objectMapper.writeValueAsString(student);
        Long ttl = 60L;

        redisService.set("1", student, ttl);

        verify(valueOperations, times(1)).set("1", studentJson, ttl, TimeUnit.SECONDS);
    }

    @Test
    void testSet_ExceptionHandling() throws Exception {
        Student student = new Student(1L, "John Doe", "123 Street");
        Long ttl = 60L;

        doThrow(new RuntimeException("Redis set error")).when(valueOperations).set(anyString(), anyString(), anyLong(), any());

        assertDoesNotThrow(() -> redisService.set("1", student, ttl)); // Ensures exception is handled gracefully
    }

    @Test
    void testGet() throws Exception {
        Student student = new Student(1L, "John Doe", "123 Street");
        String studentJson = objectMapper.writeValueAsString(student);
        when(valueOperations.get("1")).thenReturn(studentJson);

        Student result = redisService.get("1", Student.class);

        assertNotNull(result);
        assertEquals(student.getId(), result.getId());
        assertEquals(student.getName(), result.getName());
        assertEquals(student.getAddress(), result.getAddress());
    }

    @Test
    void testGet_ExceptionHandling() throws Exception {
        when(valueOperations.get("1")).thenThrow(new RuntimeException("Redis fetch error"));

        assertDoesNotThrow(() -> {
            Student result = redisService.get("1", Student.class);
            assertNull(result); // Ensure method returns null when an exception occurs
        });
    }


    @Test
    void testGet_NullResult() {
        when(valueOperations.get("1")).thenReturn(null);

        Student result = redisService.get("1", Student.class);

        assertNull(result);
    }


    @Test
    void testDelete_Success() {
        String key = "student_1";

        // Call the method under test
        redisService.delete(key);

        // ✅ Verify that `redisTemplate.delete(key)` was called exactly once
        verify(redisTemplate, times(1)).delete(key);
    }

    @Test
    void testDelete_ExceptionHandling() {
        String key = "student_2";

        // ✅ Mock an exception when calling `redisTemplate.delete(key)`
        doThrow(new RuntimeException("Redis delete error")).when(redisTemplate).delete(key);

        // Call the method under test
        redisService.delete(key);

        // ✅ Verify that `redisTemplate.delete(key)` was still called despite the exception
        verify(redisTemplate, times(1)).delete(key);
    }
}
