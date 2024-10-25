package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;
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

class SessionServiceTest {

    @InjectMocks
    private SessionService sessionService;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    private User user;
    private Session session;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User(); // Créer un utilisateur
        user.setId(1L);

        session = new Session();
        session.setId(1L);
        session.setUsers(new ArrayList<>()); // Initialiser la liste des utilisateurs
    }

    @Test
    void testCreate() {
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        Session createdSession = sessionService.create(session);

        verify(sessionRepository, times(1)).save(session);
        assertNotNull(createdSession);
        assertEquals(session, createdSession);
    }

    @Test
    void testDelete() {
        Long sessionId = 1L;

        doNothing().when(sessionRepository).deleteById(sessionId);

        sessionService.delete(sessionId);

        verify(sessionRepository, times(1)).deleteById(sessionId);
    }

    @Test
    void testFindAll() {
        List<Session> sessions = new ArrayList<>();
        when(sessionRepository.findAll()).thenReturn(sessions);

        List<Session> foundSessions = sessionService.findAll();

        verify(sessionRepository, times(1)).findAll();
        assertEquals(sessions, foundSessions);
    }

    @Test
    void testGetByIdFound() {
        Long sessionId = 1L;
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        Session foundSession = sessionService.getById(sessionId);

        verify(sessionRepository, times(1)).findById(sessionId);
        assertNotNull(foundSession);
        assertEquals(session, foundSession);
    }

    @Test
    void testGetByIdNotFound() {
        Long sessionId = 1L;
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        Session foundSession = sessionService.getById(sessionId);

        assertNull(foundSession);
    }

    @Test
    void testParticipate() {
        Long sessionId = 1L;
        Long userId = 1L;
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        sessionService.participate(sessionId, userId);

        verify(sessionRepository, times(1)).save(session);
        assertTrue(session.getUsers().contains(user));
    }

    @Test
    void testParticipateSessionNotFound() {
        Long sessionId = 1L;
        Long userId = 1L;
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            sessionService.participate(sessionId, userId);
        });
    }

    @Test
    void testParticipateUserNotFound() {
        Long sessionId = 1L;
        Long userId = 1L;
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            sessionService.participate(sessionId, userId);
        });
    }

    @Test
    void testParticipateAlreadyParticipated() {
        Long sessionId = 1L;
        Long userId = 1L;
        session.getUsers().add(user); // Ajout de l'utilisateur à la session

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> {
            sessionService.participate(sessionId, userId);
        });
    }

    @Test
    void testNoLongerParticipate() {
        Long sessionId = 1L;
        Long userId = 1L;
        session.getUsers().add(user); // Ajout de l'utilisateur à la session

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        sessionService.noLongerParticipate(sessionId, userId);

        verify(sessionRepository, times(1)).save(session);
        assertFalse(session.getUsers().contains(user));
    }

    @Test
    void testNoLongerParticipateSessionNotFound() {
        Long sessionId = 1L;
        Long userId = 1L;

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            sessionService.noLongerParticipate(sessionId, userId);
        });
    }

    @Test
    void testNoLongerParticipateUserNotRemoved() {
        Long sessionId = 1L;
        Long userId = 2L;
        User otherUser = new User();
        otherUser.setId(2L);
        session.getUsers().add(user);
        session.getUsers().add(otherUser);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        sessionService.noLongerParticipate(sessionId, userId);

        verify(sessionRepository, times(1)).save(session);
        assertTrue(session.getUsers().contains(user));
        assertFalse(session.getUsers().contains(otherUser));
    }


}
