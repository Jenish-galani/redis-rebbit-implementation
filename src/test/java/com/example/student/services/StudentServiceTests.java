package com.example.student.services;

import com.example.student.entities.Student;
import com.example.student.repositories.StudentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTests {
    @Mock
    private StudentRepository studentRepository;

    @Mock
    private RedisService redisService;

    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveOrUpdateStudentTests() {
        Student student = new Student();
        student.setId(1L);
        student.setName("Test");

        when(studentRepository.save(student)).thenReturn(student);

        Student savedStudent = studentService.saveOrUpdateStudent(student);

        assertNotNull(savedStudent);
        assertEquals(1L, savedStudent.getId());
        assertEquals("Test", savedStudent.getName());

        verify(studentRepository, times(1)).save(student);
        verify(redisService, times(1)).delete("student_1");
    }

    @Test
    void testFindAllStudentsTests() {
        // Given
        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("Test");

        Student student2 = new Student();
        student2.setId(2L);
        student2.setName("Test2");

        List<Student> mockStudents = Arrays.asList(student1, student2);
        when(studentRepository.findAll()).thenReturn(mockStudents);

        // When
        List<Student> students = studentService.findAllStudents();

        // Then
        assertNotNull(students);
        assertEquals(2, students.size());

        assertEquals(1L, students.get(0).getId());
        assertEquals("Test", students.get(0).getName());

        assertEquals(2L, students.get(1).getId());
        assertEquals("Test2", students.get(1).getName());

        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void testFindStudentById_WhenStudentNotInRedis_ButExistsInDB() {
        // Given
        Long studentId = 1L;
        Student student = new Student();
        student.setId(studentId);
        student.setName("Test");

        when(redisService.get("student_" + studentId, Student.class)).thenReturn(null);
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        // When
        Optional<Student> result = studentService.findStudentById(studentId);

        // Then
        assertTrue(result.isPresent());
        assertEquals("Test", result.get().getName());

        verify(studentRepository, times(1)).findById(studentId);
    }

    @Test
    void testFindStudentById_WhenStudentExistsInRedis() throws Exception {
        // Given
        Long studentId = 1L;
        Student student = new Student(1L, "Test", "Test address");

        // Serialize Student to JSON
        String jsonValue = new ObjectMapper().writeValueAsString(student);

        // Mock Redis to return a Student object (Not JSON String!)
        when(redisService.get("student_" + studentId, Student.class)).thenReturn(student); // ✅ Correct

        // When
        Optional<Student> result = studentService.findStudentById(studentId);

        // Then
        assertTrue(result.isPresent());
        assertEquals("Test", result.get().getName());
        assertEquals("Test address", result.get().getAddress());

        verify(redisService, times(1)).get("student_" + studentId, Student.class);
        verifyNoInteractions(studentRepository); // Ensure DB is NOT called
    }




}
