package com.srinidevlearn.productservice.websocket;

import io.swagger.v3.oas.annotations.Hidden;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@Hidden
public class ProductWebSocketController {

    private static final Logger logger = LoggerFactory.getLogger(ProductWebSocketController.class);

    @MessageMapping("/products/subscribe")
    @SendTo("/topic/products")
    public NotificationMessage subscribeToProductUpdates(String message) {
        logger.info("Client subscribed to product updates: {}", message);
        return new NotificationMessage(
                "SUBSCRIBED",
                "Successfully subscribed to product updates",
                null);
    }
}
