package com.srinidevlearn.productservice.websocket;

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
class ProductNotificationServiceTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private ProductNotificationService productNotificationService;

    @Test
    @DisplayName("notifyProductCreated should send notification to /topic/products")
    void notifyProductCreated_shouldSendNotification() {
        // Arrange
        String productId = "1";
        String productName = "Laptop";

        // Act
        productNotificationService.notifyProductCreated(productId, productName);

        // Assert
        ArgumentCaptor<NotificationMessage> captor = ArgumentCaptor.forClass(NotificationMessage.class);
        verify(messagingTemplate, times(1)).convertAndSend(eq("/topic/products"), captor.capture());
        NotificationMessage notification = captor.getValue();
        assertEquals("PRODUCT_CREATED", notification.getType());
        assertTrue(notification.getMessage().contains("Laptop"));
        assertNotNull(notification.getTimestamp());
    }

    @Test
    @DisplayName("notifyProductDeleted should send notification to /topic/products")
    void notifyProductDeleted_shouldSendNotification() {
        // Arrange
        String productId = "1";
        String productName = "Laptop";

        // Act
        productNotificationService.notifyProductDeleted(productId, productName);

        // Assert
        ArgumentCaptor<NotificationMessage> captor = ArgumentCaptor.forClass(NotificationMessage.class);
        verify(messagingTemplate, times(1)).convertAndSend(eq("/topic/products"), captor.capture());
        NotificationMessage notification = captor.getValue();
        assertEquals("PRODUCT_DELETED", notification.getType());
        assertTrue(notification.getMessage().contains("Laptop"));
    }

    @Test
    @DisplayName("notifyProductsByCategory should send notification to /topic/products")
    void notifyProductsByCategory_shouldSendNotification() {
        // Arrange
        String category = "Electronics";

        // Act
        productNotificationService.notifyProductsByCategory(category);

        // Assert
        ArgumentCaptor<NotificationMessage> captor = ArgumentCaptor.forClass(NotificationMessage.class);
        verify(messagingTemplate, times(1)).convertAndSend(eq("/topic/products"), captor.capture());
        NotificationMessage notification = captor.getValue();
        assertEquals("PRODUCTS_QUERIED", notification.getType());
        assertTrue(notification.getMessage().contains("Electronics"));
    }
}
