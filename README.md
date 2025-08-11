# 🛒 Product  Service – ZalandoLite V2 (Microservice 1)

---

### 🔗 [ZalandoLite V2  🍀 Overview Repository ](https://github.com/Ochwada/ZalandoLiteV2-MicroservicesArchitecture)

Microservices ⬇️ part of **ZalandoLite V2**

#### 🖇️ [Microservice 1: Authentication Service](https://github.com/Ochwada/ZalandoLiteV2-authentication)

#### 🖇️ [Microservice 2: Product Service](https://github.com/reyhanovelek/ZalandoLiteV2-product1)

#### 🖇️ [Microservice 3: Inventory Service](https://github.com/Ochwada/ZalandoLiteV2-inventory)

#### 🖇️ [Microservice 4: Customer Service ](https://github.com/reyhanovelek/customer-service)

#### 🖇️ [Microservice 5: Order Service](https://github.com/Ochwada/ZalandoLiteV2-order)

---

## 📖 About Product Service

This is the Product Service, a core component in a microservices-based architecture designed to manage product-related
data. It handles product creation, retrieval, and provides real-time stock availability by communicating with the
external Inventory Service.

## Features

* Store and retrieve product details: ID, name, description, price
* Communicate with Inventory Service to check stock quantity
* RESTful API design with Spring Boot
* Clean architecture using DTOs and mappers
* Configurable service URL via application properties
* Validates input using Spring's bean validation
* Environment variable support via .env file using dotenv-java
* Database configuration via application.properties
* Docker support with PostgreSQL integration

---

## API Endpoints (Quick Table)

| Method | URL                    | Purpose                  |
|-------:|------------------------|--------------------------|
|   POST | `/api/products`        | Create product           |
|    GET | `/api/products`        | List all products        |
|    GET | `/api/products/{id}`   | Get product by ID        |
|    GET | `/api/products/{name}` | Get product by name      |
|  PATCH | `/api/products/{id}`   | Partially update product |
| DELETE | `/api/products/{id}`   | Delete product           |

---

## 📦 Dependencies

Including the common dependencies (check here), some **unique dependencies** for this service are:

| Dependency Artifact                                                   | Purpose                                                                           |
|-----------------------------------------------------------------------|-----------------------------------------------------------------------------------|
| `org.springframework.boot:spring-boot-starter-data-jpa`               | Persist data in SQL stores with JPA via Spring Data and Hibernate.                |
| `org.springframework.boot:spring-boot-starter-oauth2-resource-server` | Secure the service by validating and processing OAuth2 JWT access tokens.         |
| `org.postgresql:postgresql`                                           | PostgreSQL JDBC driver – required at runtime to connect to a PostgreSQL database. |

**PostgreSQL dependency snippets**

<details>
<summary>Maven</summary>

```xml
<dependency>
  <groupId>org.postgresql</groupId>
  <artifactId>postgresql</artifactId>
  <scope>runtime</scope>
</dependency>
```

