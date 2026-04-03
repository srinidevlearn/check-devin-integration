package com.srinidevlearn.userservice.service;

import com.srinidevlearn.userservice.model.User;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final List<User> users = new ArrayList<>();

    @PostConstruct
    public void init() {
        users.add(new User(1L, "Alice Johnson", "alice@example.com", "ADMIN"));
        users.add(new User(2L, "Bob Smith", "bob@example.com", "USER"));
        users.add(new User(3L, "Charlie Brown", "charlie@example.com", "USER"));
    }

    public List<User> getAllUsers() {
        return users;
    }

    public Optional<User> getUserById(Long id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    public User createUser(User user) {
        Long nextId = users.stream()
                .mapToLong(User::getId)
                .max()
                .orElse(0L) + 1;
        user.setId(nextId);
        users.add(user);
        return user;
    }

    public Optional<User> deleteUser(Long id) {
        Optional<User> user = getUserById(id);
        user.ifPresent(users::remove);
        return user;
    }
}
