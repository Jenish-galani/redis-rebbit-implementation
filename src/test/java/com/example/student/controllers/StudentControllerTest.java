package com.example.student.controllers;

import com.example.student.controller.StudentController;
import com.example.student.entities.Student;
import com.example.student.services.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
    }

    @Test
    void testSaveOrUpdateStudent() throws Exception {
        Student student = new Student(1L, "John Doe", "123 Street");
        when(studentService.saveOrUpdateStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(post("/api/saveOrUpdateStudent")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"name\":\"John Doe\",\"address\":\"123 Street\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.address").value("123 Street"));

        verify(studentService, times(1)).saveOrUpdateStudent(any(Student.class));
    }

    @Test
    void saveOrUpdateStudentTests() {
        Student student = new Student(1L, "Test", "Test Address");

        when(studentService.saveOrUpdateStudent(any(Student.class))).thenReturn(student);

        Student savedStudent = studentController.saveOrUpdateStudent(student);

        assertNotNull(savedStudent);
        assertEquals(1L, savedStudent.getId());
        assertEquals("Test", savedStudent.getName());

        verify(studentService, times(1)).saveOrUpdateStudent(any(Student.class));
    }

    @Test
    void testFindAllStudents() throws Exception {
        Student student1 = new Student(1L, "John Doe", "123 Street");
        Student student2 = new Student(2L, "Jane Doe", "456 Avenue");
        when(studentController.findAllStudents()).thenReturn(Arrays.asList(student1, student2));

        mockMvc.perform(get("/api/findAllStudents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].name").value("Jane Doe"));

        verify(studentService, times(1)).findAllStudents();
    }

    @Test
    void testFindStudentById() throws Exception {
        Student student = new Student(1L, "John Doe", "123 Street");
        when(studentController.findStudentById(1L)).thenReturn(Optional.of(student));

        mockMvc.perform(get("/api/findStudentById/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.address").value("123 Street"));

        verify(studentService, times(1)).findStudentById(1L);
    }

}