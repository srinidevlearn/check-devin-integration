package com.srinidevlearn.userservice.event;

import java.time.Instant;

public class UserEvent {

    private String eventType;
    private String userId;
    private String userName;
    private String userEmail;
    private Instant timestamp;

    public UserEvent() {
    }

    public UserEvent(String eventType, String userId, String userName, String userEmail) {
        this.eventType = eventType;
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.timestamp = Instant.now();
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "UserEvent{eventType='" + eventType + "', userId='" + userId +
                "', userName='" + userName + "', userEmail='" + userEmail +
                "', timestamp=" + timestamp + "}";
    }
}
