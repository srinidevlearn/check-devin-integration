package com.srinidevlearn.productservice.websocket;

import java.time.Instant;

public class NotificationMessage {

    private String type;
    private String message;
    private Object payload;
    private Instant timestamp;

    public NotificationMessage() {
        this.timestamp = Instant.now();
    }

    public NotificationMessage(String type, String message, Object payload) {
        this.type = type;
        this.message = message;
        this.payload = payload;
        this.timestamp = Instant.now();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
