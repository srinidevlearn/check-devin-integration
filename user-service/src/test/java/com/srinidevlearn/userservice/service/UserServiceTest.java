package com.srinidevlearn.userservice.service;

import com.srinidevlearn.userservice.event.UserEventPublisher;
import com.srinidevlearn.userservice.model.User;
import com.srinidevlearn.userservice.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserEventPublisher userEventPublisher;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("getAllUsers should return all users from repository")
    void getAllUsers_shouldReturnAllUsers() {
        // Arrange
        List<User> expectedUsers = Arrays.asList(
                new User("1", "Alice", "alice@example.com", "ADMIN"),
                new User("2", "Bob", "bob@example.com", "USER")
        );
        when(userRepository.findAll()).thenReturn(expectedUsers);

        // Act
        List<User> actualUsers = userService.getAllUsers();

        // Assert
        assertEquals(2, actualUsers.size());
        assertEquals("Alice", actualUsers.get(0).getName());
        assertEquals("Bob", actualUsers.get(1).getName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("getUserById should return user when user exists")
    void getUserById_shouldReturnUser_whenUserExists() {
        // Arrange
        User expectedUser = new User("1", "Alice", "alice@example.com", "ADMIN");
        when(userRepository.findById("1")).thenReturn(Optional.of(expectedUser));

        // Act
        Optional<User> actualUser = userService.getUserById("1");

        // Assert
        assertTrue(actualUser.isPresent());
        assertEquals("Alice", actualUser.get().getName());
        assertEquals("alice@example.com", actualUser.get().getEmail());
        verify(userRepository, times(1)).findById("1");
    }

    @Test
    @DisplayName("getUserById should return empty when user does not exist")
    void getUserById_shouldReturnEmpty_whenUserDoesNotExist() {
        // Arrange
        when(userRepository.findById("999")).thenReturn(Optional.empty());

        // Act
        Optional<User> actualUser = userService.getUserById("999");

        // Assert
        assertFalse(actualUser.isPresent());
        verify(userRepository, times(1)).findById("999");
    }

    @Test
    @DisplayName("createUser should save user and publish event")
    void createUser_shouldSaveUserAndPublishEvent() {
        // Arrange
        User inputUser = new User(null, "Charlie", "charlie@example.com", "USER");
        User savedUser = new User("3", "Charlie", "charlie@example.com", "USER");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        User result = userService.createUser(inputUser);

        // Assert
        assertNotNull(result);
        assertEquals("3", result.getId());
        assertEquals("Charlie", result.getName());
        verify(userRepository, times(1)).save(inputUser);
        verify(userEventPublisher, times(1))
                .publishUserCreated("3", "Charlie", "charlie@example.com");
    }

    @Test
    @DisplayName("deleteUser should delete user and publish event when user exists")
    void deleteUser_shouldDeleteAndPublishEvent_whenUserExists() {
        // Arrange
        User existingUser = new User("1", "Alice", "alice@example.com", "ADMIN");
        when(userRepository.findById("1")).thenReturn(Optional.of(existingUser));

        // Act
        Optional<User> result = userService.deleteUser("1");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Alice", result.get().getName());
        verify(userRepository, times(1)).deleteById("1");
        verify(userEventPublisher, times(1))
                .publishUserDeleted("1", "Alice", "alice@example.com");
    }

    @Test
    @DisplayName("deleteUser should return empty and not publish event when user does not exist")
    void deleteUser_shouldReturnEmpty_whenUserDoesNotExist() {
        // Arrange
        when(userRepository.findById("999")).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.deleteUser("999");

        // Assert
        assertFalse(result.isPresent());
        verify(userRepository, never()).deleteById(any());
        verify(userEventPublisher, never()).publishUserDeleted(any(), any(), any());
    }
}
