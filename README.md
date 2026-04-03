# Spring Boot Microservices with API Gateway

A multi-module Spring Boot project demonstrating microservice architecture with API Gateway, Service Discovery, MongoDB persistence, and Solace event-driven messaging.

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
     └───┬────▲───────┘     │     └───┬────▲────────┘
         │    │              │         │    │
         │    │     ┌────────▼────────┐│    │
         │    └─────│   API Gateway   │┘    │
         │          │   (Port 8080)   │     │
         │          └─────────────────┘     │
         │                 ▲                │
         │                 │                │
         │              Clients             │
         │                                  │
    ┌────▼──────────────────────────────────▼────┐
    │              MongoDB Atlas                  │
    │         (microservices_db)                  │
    └────────────────┬───────────────────────────┘
                     │
    ┌────────────────▼───────────────────────────┐
    │         Solace PubSub+ (Docker)            │
    │     Event-Driven Messaging (pub/sub)       │
    └────────────────────────────────────────────┘
```

## Modules

| Module | Port | Description |
|--------|------|-------------|
| `eureka-server` | 8761 | Netflix Eureka service discovery server |
| `api-gateway` | 8080 | Spring Cloud Gateway — routes, filters, load balancing |
| `user-service` | 8081 | User management REST API with MongoDB + Solace events |
| `product-service` | 8082 | Product catalog REST API with MongoDB + Solace events |

## Tech Stack

- **Java 17** + **Spring Boot 3.2.5**
- **Gradle 8.7** (multi-module build)
- **Spring Cloud 2023.0.1** (Gateway, Eureka)
- **Spring Data MongoDB** (persistence)
- **Spring Cloud Stream + Solace Binder** (event-driven pub/sub)
- **Docker Compose** (local Solace broker)
- **JUnit 5 + Mockito** (unit tests with Arrange/Act/Assert pattern)

## Prerequisites

- Java 17+
- Gradle 8.7+ (or use the Gradle wrapper)
- MongoDB (local or Atlas)
- Docker & Docker Compose (for Solace)

## Getting Started

### Build All Modules

```bash
gradle clean build
```

### Run Tests

```bash
gradle test
```

### Start Solace PubSub+ (Docker)

```bash
docker-compose up -d
```

Solace Management Console: http://localhost:8008 (admin/admin)

### Start Services (in order)

```bash
# 1. Start Eureka Server
cd eureka-server && gradle bootRun

# 2. Start User Service
cd user-service && gradle bootRun

# 3. Start Product Service
cd product-service && gradle bootRun

# 4. Start API Gateway
cd api-gateway && gradle bootRun
```

### Verify

- Eureka Dashboard: http://localhost:8761
- Users via Gateway: http://localhost:8080/api/users
- Products via Gateway: http://localhost:8080/api/products

## Configuration

### MongoDB

Set the `MONGODB_URI` environment variable to connect to MongoDB Atlas:

```bash
export MONGODB_URI=mongodb+srv://<user>:<password>@<cluster>.mongodb.net/microservices_db
```

Default (local): `mongodb://localhost:27017/microservices_db`

### Solace

| Variable | Default | Description |
|----------|---------|-------------|
| `SOLACE_HOST` | `tcp://localhost:55555` | Solace broker SMF endpoint |
| `SOLACE_VPN` | `default` | Message VPN |
| `SOLACE_USERNAME` | `default` | Client username |
| `SOLACE_PASSWORD` | *(empty)* | Client password |

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

## Event-Driven Architecture

When users or products are created/deleted, events are published to Solace topics:

- `user/events` — `USER_CREATED`, `USER_DELETED`
- `product/events` — `PRODUCT_CREATED`, `PRODUCT_DELETED`

Consumers in each service listen and log these events. Extend the consumers to implement cross-service workflows.

## Key Concepts Demonstrated

- **Service Discovery**: Netflix Eureka for automatic service registration and discovery
- **API Gateway**: Spring Cloud Gateway as a single entry point for all microservices
- **Load Balancing**: Client-side load balancing via `lb://` URI scheme
- **Custom Filters**: Gateway logging filter for request/response monitoring
- **MongoDB Persistence**: Spring Data MongoDB with MongoRepository interfaces
- **Event-Driven Messaging**: Solace PubSub+ with Spring Cloud Stream for pub/sub
- **Docker**: Local Solace broker via Docker Compose
- **Unit Testing**: Enterprise-standard tests using Arrange/Act/Assert pattern with JUnit 5 + Mockito
- **Actuator**: Health and info endpoints for service monitoring
