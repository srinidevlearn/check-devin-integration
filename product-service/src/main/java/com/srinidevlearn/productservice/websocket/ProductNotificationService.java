package com.srinidevlearn.productservice.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProductNotificationService {

    private static final Logger logger = LoggerFactory.getLogger(ProductNotificationService.class);

    private final SimpMessagingTemplate messagingTemplate;

    public ProductNotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void notifyProductCreated(String productId, String productName) {
        NotificationMessage notification = new NotificationMessage(
                "PRODUCT_CREATED",
                "New product created: " + productName,
                new ProductPayload(productId, productName));
        logger.info("Sending WebSocket notification: {}", notification.getMessage());
        messagingTemplate.convertAndSend("/topic/products", notification);
    }

    public void notifyProductDeleted(String productId, String productName) {
        NotificationMessage notification = new NotificationMessage(
                "PRODUCT_DELETED",
                "Product deleted: " + productName,
                new ProductPayload(productId, productName));
        logger.info("Sending WebSocket notification: {}", notification.getMessage());
        messagingTemplate.convertAndSend("/topic/products", notification);
    }

    public void notifyProductsByCategory(String category) {
        NotificationMessage notification = new NotificationMessage(
                "PRODUCTS_QUERIED",
                "Products queried for category: " + category,
                new CategoryPayload(category));
        logger.info("Sending WebSocket notification: {}", notification.getMessage());
        messagingTemplate.convertAndSend("/topic/products", notification);
    }

    public record ProductPayload(String productId, String productName) {}
    public record CategoryPayload(String category) {}
}
