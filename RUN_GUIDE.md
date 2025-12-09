# Microservices Project Guide

This guide explains how to run each microservice individually and how to test the application.

## Prerequisites

- Java 17
- Maven (wrapper included in projects)
- PostgreSQL (running on localhost:5432, database `tpMicroservice`, user `postgres`, password `postgres`)

## 1. Running the Services

You need to start the services in the following order. Open a separate terminal for each service.

### Step 1: Discovery Service (Eureka)
This service acts as the registry for all other microservices.
```bash
cd discovery-service
./mvnw spring-boot:run
```
**Access:** [http://localhost:8761](http://localhost:8761)

### Step 2: API Gateway
This service routes requests to the appropriate microservices.
```bash
cd "api gateway"
./mvnw spring-boot:run
```
**Access:** [http://localhost:8887](http://localhost:8887)

### Step 3: Client Service
Manages client data.
```bash
cd client-service
./mvnw spring-boot:run
```
**Access:** [http://localhost:8081/clients](http://localhost:8081/clients)

### Step 4: Product Service (Produit)
Manages product data.
```bash
cd produit
./mvnw spring-boot:run
```
**Access:** [http://localhost:8082/produits](http://localhost:8082/produits)

### Step 5: Auth Service
Handles authentication and authorization (JWT).
```bash
cd auth-service
./mvnw spring-boot:run
```
**Access:** [http://localhost:8084/users](http://localhost:8084/users)

### Step 6: Order Service (Commande)
Manages orders and interacts with Client and Product services.
```bash
cd commande-service
./mvnw spring-boot:run
```
**Access:** [http://localhost:8083/commandes](http://localhost:8083/commandes)

---

## 2. Testing the Application

You can test the services using `curl` or a browser.

### Discovery Service
Check if all services are registered:
- URL: [http://localhost:8761](http://localhost:8761)

### Client Service
- **List Clients:** `GET http://localhost:8081/clients`
- **Get Client by ID:** `GET http://localhost:8081/clients/1`

### Product Service
- **List Products:** `GET http://localhost:8082/produits`
- **Get Product by ID:** `GET http://localhost:8082/produits/1`

### Auth Service
- **Create Role:**
  ```bash
  curl -X POST -H "Content-Type: application/json" -d '{"roleName":"ADMIN"}' http://localhost:8084/roles
  ```
- **Create User:**
  ```bash
  curl -X POST -H "Content-Type: application/json" -d '{"username":"user1","password":"123"}' http://localhost:8084/users
  ```
- **Add Role to User:**
  ```bash
  curl -X POST -H "Content-Type: application/json" -d '{"username":"user1","roleName":"ADMIN"}' http://localhost:8084/addRoleToUser
  ```
- **Login (Get Token):**
  ```bash
  curl -X POST -d "username=user1&password=123" http://localhost:8084/login
  ```

### Order Service
- **List Orders:** `GET http://localhost:8083/commandes`
- **Get Order by ID:** `GET http://localhost:8083/commandes/1`

### API Gateway
Access services through the gateway (Port 8887):
- **Clients:** `GET http://localhost:8887/CLIENT-SERVICE/clients`
- **Products:** `GET http://localhost:8887/PRODUIT-SERVICE/produits`
- **Orders:** `GET http://localhost:8887/COMMANDE-SERVICE/commandes`
