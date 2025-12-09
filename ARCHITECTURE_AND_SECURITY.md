# Microservices Architecture & Security Explanation

## 1. Global Architecture Overview

This project implements a **Microservices Architecture** using the Spring Boot ecosystem. The system is composed of several independent services that collaborate to provide the full functionality.

### The Services
1.  **Discovery Service (Eureka)**: The phonebook of the system. All services register themselves here so they can find each other without hardcoded URLs.
2.  **API Gateway**: The single entry point for all client requests. It routes traffic to the appropriate microservice and handles cross-cutting concerns like **Security**.
3.  **Auth Service**: Responsible for managing users, roles, and generating **JWT Tokens**.
4.  **Client Service**: Manages client data.
5.  **Product Service**: Manages product inventory.
6.  **Order Service (Commande)**: Manages orders. It orchestrates data from Client and Product services.

---

## 2. How Microservices Communicate

There are two main types of communication used in this project:

### A. External Communication (Client to System)
*   **Via API Gateway**: Clients (Postman, Frontend, Mobile App) do not talk to microservices directly. They send requests to the **API Gateway** (port 8887).
*   **Routing**: The Gateway uses the **Discovery Service** to find the IP and Port of the target service (e.g., `client-service`) and forwards the request.

### B. Internal Communication (Service to Service)
*   **Synchronous (Feign Client)**: The **Order Service** needs data from the Client and Product services to create an order.
    *   Instead of making a raw HTTP request, it uses **OpenFeign**.
    *   It defines an interface (e.g., `ClientRestClient`) that looks like a normal Java method call.
    *   Feign automatically calls Eureka to find the `CLIENT-SERVICE`, load-balances the request, and returns the data.

**Example Flow: Creating an Order**
1.  User sends `POST /commandes` to Gateway.
2.  Gateway forwards to Order Service.
3.  Order Service calls `ClientRestClient.findClientById(id)`.
4.  Order Service calls `ProductRestClient.findProductById(id)`.
5.  If data is valid, the Order is saved.

---

## 3. Security Architecture (Detailed)

Security is implemented using a **Centralized Authentication** pattern with **JWT (JSON Web Tokens)**.

### The Components

#### 1. Auth Service ( The Identity Provider)
*   **Responsibility**: Verify credentials and issue tokens.
*   **Database**: Stores `AppUser` and `AppRole` in the PostgreSQL database.
*   **Key Endpoint**: `/login`
    *   User sends `username` & `password`.
    *   Service verifies password (encrypted with BCrypt).
    *   If valid, it generates a **JWT Signed Token**.
    *   **The Token contains**:
        *   `sub`: Username (e.g., "admin")
        *   `roles`: List of user roles (e.g., "ADMIN", "USER")
        *   `exp`: Expiration time (e.g., 5 minutes)
        *   **Signature**: A cryptographic signature using a **Secret Key**.

#### 2. API Gateway (The Gatekeeper)
*   **Responsibility**: Validate tokens before allowing access to internal services.
*   **Mechanism**: A Global Filter (`AuthFilter.java`).
*   **The Process**:
    1.  **Intercept**: Every request hitting the Gateway is intercepted.
    2.  **Check Whitelist**: If the request is for `/login` or registration, it is allowed through immediately.
    3.  **Check Header**: For all other requests, it looks for the `Authorization: Bearer <token>` header.
    4.  **Validate**: It parses the JWT using the **Same Secret Key** as the Auth Service.
        *   If the signature is invalid (token tampered with) -> **401 Unauthorized**.
        *   If the token is expired -> **401 Unauthorized**.
    5.  **Forward**: If valid, the request is forwarded to the destination service (e.g., Client Service).

### Why this approach?
*   **Stateless**: Microservices don't need to store session data. The token contains everything needed.
*   **Decoupled**: The Client/Product/Order services **do not** need to implement complex security logic. They trust the Gateway. If a request reaches them, it means the Gateway has already verified the user.

### Security Flow Diagram

```
[Client] 
   | 
   | (1) POST /login (user/pass)
   v
[API Gateway] ----(2) Forward---> [Auth Service]
                                       |
                                       | (3) Verify Creds & Generate JWT
                                       v
[Client] <----(4) Return JWT-----------|
   |
   | (5) GET /clients (Header: Bearer JWT)
   v
[API Gateway]
   | (6) Validate JWT Signature
   | (7) If Valid: Forward Request
   v
[Client Service]
   | (8) Return Data
   v
[API Gateway]
   |
   v
[Client]
```
