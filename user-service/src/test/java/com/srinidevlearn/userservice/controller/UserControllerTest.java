package com.srinidevlearn.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.srinidevlearn.userservice.model.User;
import com.srinidevlearn.userservice.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/users should return all users with 200 OK")
    void getAllUsers_shouldReturnAllUsers() throws Exception {
        // Arrange
        List<User> users = Arrays.asList(
                new User("1", "Alice", "alice@example.com", "ADMIN"),
                new User("2", "Bob", "bob@example.com", "USER")
        );
        when(userService.getAllUsers()).thenReturn(users);

        // Act & Assert
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Alice"))
                .andExpect(jsonPath("$[1].name").value("Bob"));
    }

    @Test
    @DisplayName("GET /api/users/{id} should return user when found")
    void getUserById_shouldReturnUser_whenFound() throws Exception {
        // Arrange
        User user = new User("1", "Alice", "alice@example.com", "ADMIN");
        when(userService.getUserById("1")).thenReturn(Optional.of(user));

        // Act & Assert
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.email").value("alice@example.com"));
    }

    @Test
    @DisplayName("GET /api/users/{id} should return 404 when not found")
    void getUserById_shouldReturn404_whenNotFound() throws Exception {
        // Arrange
        when(userService.getUserById("999")).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/users should create user and return 201")
    void createUser_shouldCreateAndReturn201() throws Exception {
        // Arrange
        User inputUser = new User(null, "Charlie", "charlie@example.com", "USER");
        User savedUser = new User("3", "Charlie", "charlie@example.com", "USER");
        when(userService.createUser(any(User.class))).thenReturn(savedUser);

        // Act & Assert
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("3"))
                .andExpect(jsonPath("$.name").value("Charlie"));
    }

    @Test
    @DisplayName("DELETE /api/users/{id} should return 204 when user exists")
    void deleteUser_shouldReturn204_whenExists() throws Exception {
        // Arrange
        User user = new User("1", "Alice", "alice@example.com", "ADMIN");
        when(userService.deleteUser("1")).thenReturn(Optional.of(user));

        // Act & Assert
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/users/{id} should return 404 when user does not exist")
    void deleteUser_shouldReturn404_whenNotFound() throws Exception {
        // Arrange
        when(userService.deleteUser("999")).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(delete("/api/users/999"))
                .andExpect(status().isNotFound());
    }
}
