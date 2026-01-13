# Microservices-Based E-Commerce System
## Advanced Software Architecture Project

**Institution:** Ecole Polytechnique Sousse  
**Supervisor:** Prof. Haythem Saoudi  
**Author:** Amine Ouhiba

---

## Project Overview
This project implements a distributed e-commerce architecture using Spring Boot and Spring Cloud. The system is designed with high scalability and modularity, featuring service discovery, centralized API management, JWT-based security, and advanced AI integration via the Model Context Protocol (MCP).

## System Architecture

### Core Components
1. **Discovery Service (Eureka):** Manages service registration and health monitoring.
2. **API Gateway:** Central entry point handling request routing, security filtering, and CORS policies.
3. **Authentication Service:** Manages user identity, role-based access control (RBAC), and JWT issuance.
4. **Client Service:** Manages customer records and exposes MCP-compliant tools.
5. **Product Service:** Handles inventory management and provides product data through MCP.
6. **Order Service:** Orchestrates transaction processing and order history.
7. **AI Agent Service:** Integrates local LLMs (Ollama) with business logic using the Model Context Protocol.

### Communication Flow
- **Service Discovery:** All microservices register with the Eureka server upon startup.
- **Internal Communication:** Services interact via OpenFeign for synchronous RESTful calls.
- **External Access:** All client requests are validated at the Gateway using JWT tokens.

## Technology Stack

### Backend Frameworks
- **Java 17** (OpenJDK)
- **Spring Boot 3.3.5**
- **Spring Cloud 2023.0.3**
- **Spring AI 1.1.1**
- **Spring Security** (JWT Implementation)

### Persistence and AI
- **PostgreSQL:** Relational database management.
- **Ollama:** Local Large Language Model runtime.
- **Model Context Protocol (MCP):** Standardized interface for AI-to-service communication.

### Frontend
- **Angular 20**
- **TypeScript**
- **RxJS**

## Deployment and Execution

### Prerequisites
- Java Development Kit (JDK) 17+
- Apache Maven 3.6+
- PostgreSQL Server (Default port: 5432)
- Node.js and npm (For frontend execution)
- Ollama Runtime (For AI functionalities)

### Installation
1. **Database Initialization:**
   Create a PostgreSQL database named `tpMicroservice`.
2. **Build Process:**
   ```bash
   mvn clean package -DskipTests
   ```

### Execution Order
For optimal system stability, services should be started in the following sequence:
1. Discovery Service (Port 8761)
2. API Gateway (Port 8888)
3. Auth Service (Port 8080)
4. Client, Product, and Order Services
5. AI Agent Service (Port 8081)

Startup scripts (`start-all.sh`) are provided for automated deployment.

## API Documentation

### Authentication
**Endpoint:** `POST /auth-service/login`  
**Payload:** `{ "username": "...", "password": "..." }`  
**Response:** Returns a Bearer token required for all subsequent requests.

### Protected Resources
All business logic endpoints require an `Authorization` header:  
`Authorization: Bearer <JWT_TOKEN>`

### AI Interface
**Endpoint:** `GET /agent-ia-service/ask?message=<query>`  
The AI agent evaluates the query and may autonomously trigger MCP tools from the Client or Product services to provide context-aware responses.

## License
Internal Project - Ecole Polytechnique Sousse
