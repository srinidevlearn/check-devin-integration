# Spring Boot Microservices with API Gateway

A multi-module Spring Boot project demonstrating microservice architecture with API Gateway and Service Discovery patterns.

## Architecture

```
                    ┌─────────────────┐
                    │  Eureka Server  │
                    │   (Port 8761)   │
                    └────────┬────────┘
                             │ Service Registry
              ┌──────────────┼──────────────┐
              │              │              │
     ┌────────▼───────┐     │     ┌────────▼────────┐
     │  User Service   │     │     │ Product Service  │
     │  (Port 8081)    │     │     │  (Port 8082)     │
     └────────▲───────┘     │     └────────▲────────┘
              │              │              │
              │     ┌────────▼────────┐     │
              └─────│   API Gateway   │─────┘
                    │   (Port 8080)   │
                    └─────────────────┘
                         ▲
                         │
                      Clients
```

## Modules

| Module | Port | Description |
|--------|------|-------------|
| `eureka-server` | 8761 | Netflix Eureka service discovery server |
| `api-gateway` | 8080 | Spring Cloud Gateway — routes, filters, load balancing |
| `user-service` | 8081 | Sample REST microservice for user management |
| `product-service` | 8082 | Sample REST microservice for product catalog |

## Prerequisites

- Java 17+
- Maven 3.6+

## Getting Started

### Build All Modules

```bash
mvn clean install
```

### Start Services (in order)

```bash
# 1. Start Eureka Server
cd eureka-server && mvn spring-boot:run

# 2. Start User Service
cd user-service && mvn spring-boot:run

# 3. Start Product Service
cd product-service && mvn spring-boot:run

# 4. Start API Gateway
cd api-gateway && mvn spring-boot:run
```

### Verify

- Eureka Dashboard: http://localhost:8761
- Users via Gateway: http://localhost:8080/api/users
- Products via Gateway: http://localhost:8080/api/products

## API Endpoints

### User Service (`/api/users`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/users` | List all users |
| GET | `/api/users/{id}` | Get user by ID |
| POST | `/api/users` | Create a new user |
| DELETE | `/api/users/{id}` | Delete a user |

### Product Service (`/api/products`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/products` | List all products |
| GET | `/api/products/{id}` | Get product by ID |
| GET | `/api/products/category/{category}` | Get products by category |
| POST | `/api/products` | Create a new product |
| DELETE | `/api/products/{id}` | Delete a product |

## Key Concepts Demonstrated

- **Service Discovery**: Netflix Eureka for automatic service registration and discovery
- **API Gateway**: Spring Cloud Gateway as a single entry point for all microservices
- **Load Balancing**: Client-side load balancing via `lb://` URI scheme
- **Custom Filters**: Gateway logging filter for request/response monitoring
- **Actuator**: Health and info endpoints for service monitoring
