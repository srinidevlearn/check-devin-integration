# Testing Spring Boot Microservices

## Project Overview
Gradle 8.7 multi-module Spring Boot 3.2.5 project with 4 modules: eureka-server, api-gateway, user-service, product-service.

## Build & Test Commands
```bash
# Build all modules (skip tests)
gradle clean build -x test

# Run all unit tests
gradle test

# Run tests for a specific module
gradle :user-service:test
gradle :product-service:test
```

## Unit Test Details
- Tests use JUnit 5 + Mockito with Arrange/Act/Assert pattern
- Controller tests use `@WebMvcTest` with `@MockBean` (package: `org.springframework.boot.test.mock.mockito.MockBean` for Spring Boot 3.2.x)
- Service tests use `@ExtendWith(MockitoExtension.class)` with `@Mock` and `@InjectMocks`
- All tests are mocked — no real MongoDB or Solace needed for unit tests
- Test reports are generated at `{module}/build/reports/tests/test/index.html`

## Infrastructure Requirements for Runtime Testing

### MongoDB
- Required for services to start at runtime
- Set `MONGODB_URI` env var (default: `mongodb://localhost:27017/microservices_db`)
- For Atlas: `export MONGODB_URI=mongodb+srv://<user>:<password>@<cluster>.mongodb.net/microservices_db`
- MongoDB is NOT installed on the Devin VM by default

### Solace PubSub+
- Start local broker: `docker compose up -d` (uses `docker-compose.yml` at repo root)
- Solace image is large (~1GB), first pull takes time
- Management console: http://localhost:8008 (admin/admin)
- SMF port: 55555, REST port: 9000
- Set `SOLACE_HOST` env var if not using default `tcp://localhost:55555`

## Service Startup Order
1. Start Solace: `docker compose up -d`
2. Start Eureka: `(cd eureka-server && gradle bootRun)`
3. Wait ~15s for Eureka to be ready
4. Start User Service: `(cd user-service && gradle bootRun)`
5. Start Product Service: `(cd product-service && gradle bootRun)`
6. Start API Gateway: `(cd api-gateway && gradle bootRun)`

## Verification Endpoints
- Eureka Dashboard: http://localhost:8761
- Users via Gateway: http://localhost:8080/api/users
- Products via Gateway: http://localhost:8080/api/products

## Common Issues
- `@MockBean` import: Use `org.springframework.boot.test.mock.mockito.MockBean` (NOT `org.springframework.boot.test.mock.bean.MockBean`) for Spring Boot 3.2.x
- Docker Compose `version` attribute warning is cosmetic and non-blocking
- Services will fail to start without MongoDB — unit tests still work fine since they use mocks
- Gradle deprecation warnings about incompatibility with Gradle 9.0 are non-blocking

## Devin Secrets Needed
- `MONGODB_URI` — MongoDB Atlas connection string (needed only for runtime E2E testing, not unit tests)
