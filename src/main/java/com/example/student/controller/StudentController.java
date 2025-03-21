package com.example.student.controller;

import com.example.student.entities.Student;
import com.example.student.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class StudentController {
    @Autowired
    private StudentService studentService;



    @PostMapping("/saveOrUpdateStudent")
    public Student saveOrUpdateStudent(@RequestBody Student student) {
        return studentService.saveOrUpdateStudent(student);
    }

    @GetMapping("/findAllStudents")
    public List<Student> findAllStudents() {
        return studentService.findAllStudents();
    }

    @GetMapping("/findStudentById/{id}")
    public Optional<Student> findStudentById(@PathVariable Long id) {
        return studentService.findStudentById(id);
    }
}
