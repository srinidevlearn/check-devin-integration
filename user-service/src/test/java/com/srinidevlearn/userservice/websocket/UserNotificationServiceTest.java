package com.srinidevlearn.userservice.websocket;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserNotificationServiceTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private UserNotificationService userNotificationService;

    @Test
    @DisplayName("notifyUserCreated should send notification to /topic/users")
    void notifyUserCreated_shouldSendNotification() {
        // Arrange
        String userId = "1";
        String userName = "Alice";

        // Act
        userNotificationService.notifyUserCreated(userId, userName);

        // Assert
        ArgumentCaptor<NotificationMessage> captor = ArgumentCaptor.forClass(NotificationMessage.class);
        verify(messagingTemplate, times(1)).convertAndSend(eq("/topic/users"), captor.capture());
        NotificationMessage notification = captor.getValue();
        assertEquals("USER_CREATED", notification.getType());
        assertTrue(notification.getMessage().contains("Alice"));
        assertNotNull(notification.getTimestamp());
    }

    @Test
    @DisplayName("notifyUserDeleted should send notification to /topic/users")
    void notifyUserDeleted_shouldSendNotification() {
        // Arrange
        String userId = "1";
        String userName = "Alice";

        // Act
        userNotificationService.notifyUserDeleted(userId, userName);

        // Assert
        ArgumentCaptor<NotificationMessage> captor = ArgumentCaptor.forClass(NotificationMessage.class);
        verify(messagingTemplate, times(1)).convertAndSend(eq("/topic/users"), captor.capture());
        NotificationMessage notification = captor.getValue();
        assertEquals("USER_DELETED", notification.getType());
        assertTrue(notification.getMessage().contains("Alice"));
    }

    @Test
    @DisplayName("notifySpecificUser should send to user-specific queue")
    void notifySpecificUser_shouldSendToUserQueue() {
        // Arrange
        String userId = "42";
        NotificationMessage notification = new NotificationMessage("CUSTOM", "Test message", null);

        // Act
        userNotificationService.notifySpecificUser(userId, notification);

        // Assert
        verify(messagingTemplate, times(1))
                .convertAndSend(eq("/queue/user/42"), eq(notification));
    }
}
