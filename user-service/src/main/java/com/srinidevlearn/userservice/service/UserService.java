package com.srinidevlearn.userservice.service;

import com.srinidevlearn.userservice.event.UserEventPublisher;
import com.srinidevlearn.userservice.model.User;
import com.srinidevlearn.userservice.repository.UserRepository;
import com.srinidevlearn.userservice.websocket.UserNotificationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserEventPublisher userEventPublisher;
    private final UserNotificationService userNotificationService;

    public UserService(UserRepository userRepository, UserEventPublisher userEventPublisher,
                       UserNotificationService userNotificationService) {
        this.userRepository = userRepository;
        this.userEventPublisher = userEventPublisher;
        this.userNotificationService = userNotificationService;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        User saved = userRepository.save(user);
        userEventPublisher.publishUserCreated(saved.getId(), saved.getName(), saved.getEmail());
        userNotificationService.notifyUserCreated(saved.getId(), saved.getName());
        return saved;
    }

    public Optional<User> deleteUser(String id) {
        Optional<User> user = userRepository.findById(id);
        user.ifPresent(u -> {
            userRepository.deleteById(id);
            userEventPublisher.publishUserDeleted(u.getId(), u.getName(), u.getEmail());
            userNotificationService.notifyUserDeleted(u.getId(), u.getName());
        });
        return user;
    }
}
