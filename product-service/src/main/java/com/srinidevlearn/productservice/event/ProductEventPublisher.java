package com.srinidevlearn.productservice.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class ProductEventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(ProductEventPublisher.class);
    private static final String PRODUCT_EVENTS_BINDING = "productEvents-out-0";

    private final StreamBridge streamBridge;

    public ProductEventPublisher(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public void publishProductCreated(String productId, String productName, String category) {
        ProductEvent event = new ProductEvent("PRODUCT_CREATED", productId, productName, category);
        logger.info("Publishing product created event: {}", event);
        streamBridge.send(PRODUCT_EVENTS_BINDING, event);
    }

    public void publishProductDeleted(String productId, String productName, String category) {
        ProductEvent event = new ProductEvent("PRODUCT_DELETED", productId, productName, category);
        logger.info("Publishing product deleted event: {}", event);
        streamBridge.send(PRODUCT_EVENTS_BINDING, event);
    }
}
