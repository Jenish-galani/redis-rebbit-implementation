package com.example.student.services;

import com.example.student.entities.Student;
import com.example.student.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RedisService redisService;

    public Student saveOrUpdateStudent(Student student) {
            Student savedStudent = studentRepository.save(student);

//         Remove the old cached record from Redis
        String redisKey = "student_" + student.getId();
        redisService.delete(redisKey);

        return savedStudent;
    }


    public List<Student> findAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> findStudentById(Long id) {
        Student student = redisService.get("student_" + id, Student.class);

        if (student != null) {
            return Optional.of(student);
        }

        // Fetch from DB if not in Redis
        Optional<Student> studentFromDB = studentRepository.findById(id);

        // Store in Redis if found
        studentFromDB.ifPresent(s -> redisService.set("student_" + id, s, 300L));

        return studentFromDB;
    }



}
