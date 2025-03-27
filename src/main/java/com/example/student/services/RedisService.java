package com.example.student.services;

import com.example.student.entities.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate; // Use proper generics

    private final ObjectMapper objectMapper = new ObjectMapper(); // Create once and reuse

    public Student get(String id, Class<Student> studentClass) {
        try {
            String jsonValue = redisTemplate.opsForValue().get(id);
            if (jsonValue != null) {
                return objectMapper.readValue(jsonValue, studentClass);
            }
        } catch (Exception e) {
            log.error("Exception while retrieving from Redis", e);
        }
        return null;
    }

    public void set(String id, Student student, Long ttl) {
        try {
            String jsonValue = objectMapper.writeValueAsString(student);
            redisTemplate.opsForValue().set(id, jsonValue, ttl, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Exception while saving to Redis", e);
        }
    }


    public void delete(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            log.error("Exception while deleting from Redis", e);
        }
    }

}
