package com.srinidevlearn.userservice.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class UserEventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(UserEventPublisher.class);
    private static final String USER_EVENTS_BINDING = "userEvents-out-0";

    private final StreamBridge streamBridge;

    public UserEventPublisher(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public void publishUserCreated(String userId, String userName, String userEmail) {
        UserEvent event = new UserEvent("USER_CREATED", userId, userName, userEmail);
        logger.info("Publishing user created event: {}", event);
        streamBridge.send(USER_EVENTS_BINDING, event);
    }

    public void publishUserDeleted(String userId, String userName, String userEmail) {
        UserEvent event = new UserEvent("USER_DELETED", userId, userName, userEmail);
        logger.info("Publishing user deleted event: {}", event);
        streamBridge.send(USER_EVENTS_BINDING, event);
    }
}
