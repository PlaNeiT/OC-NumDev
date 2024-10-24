package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TeacherServiceTest {

    @InjectMocks
    private TeacherService teacherService;

    @Mock
    private TeacherRepository teacherRepository;

    private Teacher teacher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        teacher = new Teacher();  // Créer un enseignant de test
        teacher.setId(1L);
    }

    @Test
    void testFindAll() {
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(teacher);

        // Mock de la méthode findAll() pour retourner la liste des enseignants
        when(teacherRepository.findAll()).thenReturn(teachers);

        // Appel de la méthode à tester
        List<Teacher> foundTeachers = teacherService.findAll();

        // Vérifications
        verify(teacherRepository, times(1)).findAll();
        assertNotNull(foundTeachers);
        assertEquals(teachers.size(), foundTeachers.size());
        assertEquals(teachers.get(0), foundTeachers.get(0));
    }

    @Test
    void testFindByIdFound() {
        Long teacherId = 1L;

        // Mock de la méthode findById() pour retourner un enseignant
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));

        // Appel de la méthode à tester
        Teacher foundTeacher = teacherService.findById(teacherId);

        // Vérifications
        verify(teacherRepository, times(1)).findById(teacherId);
        assertNotNull(foundTeacher);
        assertEquals(teacher, foundTeacher);
    }

    @Test
    void testFindByIdNotFound() {
        Long teacherId = 1L;

        // Mock de la méthode findById() pour retourner un Optional vide
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());

        // Appel de la méthode à tester
        Teacher foundTeacher = teacherService.findById(teacherId);

        // Vérifications
        verify(teacherRepository, times(1)).findById(teacherId);
        assertNull(foundTeacher);  // Doit retourner null si aucun enseignant trouvé
    }
}
