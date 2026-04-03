package com.srinidevlearn.userservice.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserNotificationService {

    private static final Logger logger = LoggerFactory.getLogger(UserNotificationService.class);

    private final SimpMessagingTemplate messagingTemplate;

    public UserNotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void notifyUserCreated(String userId, String userName) {
        NotificationMessage notification = new NotificationMessage(
                "USER_CREATED",
                "New user created: " + userName,
                new UserPayload(userId, userName));
        logger.info("Sending WebSocket notification: {}", notification.getMessage());
        messagingTemplate.convertAndSend("/topic/users", notification);
    }

    public void notifyUserDeleted(String userId, String userName) {
        NotificationMessage notification = new NotificationMessage(
                "USER_DELETED",
                "User deleted: " + userName,
                new UserPayload(userId, userName));
        logger.info("Sending WebSocket notification: {}", notification.getMessage());
        messagingTemplate.convertAndSend("/topic/users", notification);
    }

    public void notifySpecificUser(String userId, NotificationMessage notification) {
        logger.info("Sending private notification to user {}: {}", userId, notification.getMessage());
        messagingTemplate.convertAndSendToUser(userId, "/queue/notifications", notification);
    }

    public record UserPayload(String userId, String userName) {}
}
