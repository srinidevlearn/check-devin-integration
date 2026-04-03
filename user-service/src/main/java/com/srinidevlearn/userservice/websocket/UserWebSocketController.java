package com.srinidevlearn.userservice.websocket;

import io.swagger.v3.oas.annotations.Hidden;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@Hidden
public class UserWebSocketController {

    private static final Logger logger = LoggerFactory.getLogger(UserWebSocketController.class);

    @MessageMapping("/users/subscribe")
    @SendTo("/topic/users")
    public NotificationMessage subscribeToUserUpdates(String message) {
        logger.info("Client subscribed to user updates: {}", message);
        return new NotificationMessage(
                "SUBSCRIBED",
                "Successfully subscribed to user updates",
                null);
    }
}
