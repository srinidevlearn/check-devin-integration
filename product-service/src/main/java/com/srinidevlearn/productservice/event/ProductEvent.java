package com.srinidevlearn.productservice.event;

import java.time.Instant;

public class ProductEvent {

    private String eventType;
    private String productId;
    private String productName;
    private String category;
    private Instant timestamp;

    public ProductEvent() {
    }

    public ProductEvent(String eventType, String productId, String productName, String category) {
        this.eventType = eventType;
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.timestamp = Instant.now();
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ProductEvent{eventType='" + eventType + "', productId='" + productId +
                "', productName='" + productName + "', category='" + category +
                "', timestamp=" + timestamp + "}";
    }
}
