package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        long userId = 1L;
        user = new User(userId, "Will.Smith@gmail.com", "Smith", "Will", "password", false, LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    public void testFindById_UserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User foundUser = userService.findById(1L);

        assertNotNull(foundUser);
        assertEquals("Will.Smith@gmail.com", foundUser.getEmail());
        assertEquals("Will", foundUser.getFirstName());
        assertEquals("Smith", foundUser.getLastName());
    }

    @Test
    public void testFindById_UserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        User foundUser = userService.findById(1L);

        assertNull(foundUser);
    }

    @Test
    public void testDelete_UserExists() {
        userService.delete(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
}
