package com.srinidevlearn.productservice.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class ProductEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(ProductEventConsumer.class);

    @Bean
    public Consumer<ProductEvent> productEventListener() {
        return event -> logger.info("Received product event: {}", event);
    }
}
