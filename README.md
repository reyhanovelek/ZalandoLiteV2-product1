# 🛒 Product  Service – ZalandoLite V2 (Microservice 1)

---

###   🔗 [ZalandoLite V2  🍀 Overview Repository ](https://github.com/Ochwada/ZalandoLiteV2-MicroservicesArchitecture)
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
- Store and retrieve product details: ID, name, description, price 
- Communicate with Inventory Service to check stock quantity 
- RESTful API design with Spring Boot 
- Clean architecture using DTOs and mappers 
- Configurable service URL via application properties 
- Validates input using Spring's bean validation 
- Environment variable support via .env file using dotenv-java 
- Database configuration via application.properties 
- Docker support with PostgreSQL integration