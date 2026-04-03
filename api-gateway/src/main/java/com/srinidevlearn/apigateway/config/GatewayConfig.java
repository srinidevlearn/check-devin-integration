package com.srinidevlearn.apigateway.config;

import com.srinidevlearn.apigateway.filter.LoggingFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    private final LoggingFilter loggingFilter;

    public GatewayConfig(LoggingFilter loggingFilter) {
        this.loggingFilter = loggingFilter;
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r -> r
                        .path("/api/users/**")
                        .filters(f -> f.filter(loggingFilter.apply(new LoggingFilter.Config())))
                        .uri("lb://USER-SERVICE"))
                .route("product-service", r -> r
                        .path("/api/products/**")
                        .filters(f -> f.filter(loggingFilter.apply(new LoggingFilter.Config())))
                        .uri("lb://PRODUCT-SERVICE"))
                .build();
    }
}
