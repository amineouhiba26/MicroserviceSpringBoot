<p align="center">
  <img src="https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 17"/>
  <img src="https://img.shields.io/badge/Spring_Boot-3.3.5-brightgreen?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot"/>
  <img src="https://img.shields.io/badge/Spring_Cloud-2023.0.3-blue?style=for-the-badge&logo=spring&logoColor=white" alt="Spring Cloud"/>
  <img src="https://img.shields.io/badge/Angular-20-red?style=for-the-badge&logo=angular&logoColor=white" alt="Angular"/>
  <img src="https://img.shields.io/badge/PostgreSQL-Latest-blue?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL"/>
  <img src="https://img.shields.io/badge/Spring_AI-1.1.1-purple?style=for-the-badge&logo=spring&logoColor=white" alt="Spring AI"/>
</p>

<h1 align="center">🚀 Spring Boot Microservices Architecture</h1>

<p align="center">
  <strong>A comprehensive microservices e-commerce platform built with Spring Boot, Spring Cloud, JWT Security, and AI Integration using MCP (Model Context Protocol)</strong>
</p>

<p align="center">
  <a href="#-features">Features</a> •
  <a href="#-architecture">Architecture</a> •
  <a href="#-tech-stack">Tech Stack</a> •
  <a href="#-getting-started">Getting Started</a> •
  <a href="#-api-reference">API Reference</a> •
  <a href="#-license">License</a>
</p>

---

## ✨ Features

- 🔍 **Service Discovery** - Netflix Eureka for automatic service registration and discovery
- 🌐 **API Gateway** - Centralized routing, security, and CORS handling with Spring Cloud Gateway
- 🔐 **JWT Authentication** - Secure token-based authentication with role-based access control
- 🔗 **Inter-Service Communication** - Seamless communication using OpenFeign
- 🤖 **AI Integration** - Intelligent chatbot powered by Spring AI, Ollama, and MCP (Model Context Protocol)
- 💾 **PostgreSQL Database** - Robust and reliable data persistence
- 🎨 **Angular Frontend** - Modern, responsive user interface built with Angular 20

---

## 🏗️ Architecture

```
                                    ┌─────────────────────────────────────┐
                                    │       🔍 Discovery Service          │
                                    │         (Eureka - 8761)             │
                                    └──────────────┬──────────────────────┘
                                                   │ Register/Discover
                    ┌──────────────────────────────┼──────────────────────────────┐
                    │                              │                              │
┌───────────────────▼───┐    ┌────────────────────▼────┐    ┌────────────────────▼───┐
│  👤 Client Service    │    │   📦 Product Service    │    │  🛒 Order Service      │
│   (8082) - MCP Server │    │    (9081) - MCP Server  │    │   (9092) - MCP Server  │
└───────────────────────┘    └─────────────────────────┘    └────────────────────────┘
           ▲                            ▲                              ▲
           │                            │                              │
           │         ┌──────────────────┴──────────────────┐           │
           │         │      🌐 API Gateway (8888)          │           │
           └─────────┤   • JWT Validation                  ├───────────┘
                     │   • Request Routing                 │
                     │   • CORS Handling                   │
                     └─────────────────┬───────────────────┘
                                       │
           ┌───────────────────────────┼───────────────────────────┐
           │                           │                           │
┌──────────▼──────────┐    ┌───────────▼───────────┐    ┌─────────▼──────────┐
│  🔐 Auth Service    │    │   🤖 AI Agent Service │    │   🎨 Frontend      │
│   (8080)            │    │    (8081) - MCP Client│    │   (Angular 20)     │
│   • JWT Generation  │    │    • Ollama/LLM       │    │                    │
└─────────────────────┘    └───────────────────────┘    └────────────────────┘
```

### 📋 Services Overview

| Service | Port | Description |
|---------|------|-------------|
| **Discovery Service** | `8761` | Eureka Server - Service registration and discovery |
| **API Gateway** | `8888` | Centralized entry point - routing, JWT validation, CORS |
| **Auth Service** | `8080` | Authentication & authorization - JWT token generation |
| **Client Service** | `8082` | Customer management with MCP server capabilities |
| **Product Service** | `9081` | Product inventory management with MCP server |
| **Order Service** | `9092` | Order processing and management with MCP server |
| **AI Agent Service** | `8081` | AI-powered chatbot with MCP client integration |
| **Frontend** | `4200` | Angular 20 web application |

---

## 🛠️ Tech Stack

### Backend
| Technology | Version | Purpose |
|------------|---------|---------|
| ![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk) | 17 | Core programming language |
| ![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3.5-brightgreen?logo=springboot) | 3.3.5 | Application framework |
| ![Spring Cloud](https://img.shields.io/badge/Spring_Cloud-2023.0.3-blue?logo=spring) | 2023.0.3 | Microservices infrastructure |
| ![Spring AI](https://img.shields.io/badge/Spring_AI-1.1.1-purple?logo=spring) | 1.1.1 | AI/LLM integration |
| ![Spring Security](https://img.shields.io/badge/Spring_Security-Latest-green?logo=springsecurity) | Latest | Security framework |
| ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Latest-blue?logo=postgresql) | Latest | Relational database |
| ![JWT](https://img.shields.io/badge/JWT-0.12.6-black?logo=jsonwebtokens) | 0.12.6 | Token-based authentication |

### Frontend
| Technology | Version | Purpose |
|------------|---------|---------|
| ![Angular](https://img.shields.io/badge/Angular-20-red?logo=angular) | 20.2.0 | Frontend framework |
| ![TypeScript](https://img.shields.io/badge/TypeScript-5.9-blue?logo=typescript) | 5.9.2 | Type-safe JavaScript |
| ![RxJS](https://img.shields.io/badge/RxJS-7.8-purple?logo=reactivex) | 7.8.0 | Reactive programming |

### AI & Tools
| Technology | Purpose |
|------------|---------|
| ![Ollama](https://img.shields.io/badge/Ollama-Local_LLM-black) | Local LLM runtime |
| ![MCP](https://img.shields.io/badge/MCP-Model_Context_Protocol-blue) | AI-service integration |

---

## 📁 Project Structure

```
MicroserviceSpringBoot/
├── 📦 pom.xml                       # Parent POM (Multi-module Maven project)
├── 📖 README.md                     # This file
├── 📖 PROJECT_DOCUMENTATION.md      # Detailed technical documentation
├── 📖 RUN_GUIDE.md                  # Step-by-step run instructions
├── 🔧 start-all.sh                  # Script to start all services
├── 🔧 stop-all.sh                   # Script to stop all services
│
├── 🔍 discovery-service/            # Netflix Eureka Server
│   ├── pom.xml
│   └── src/main/java/.../DemoApplication.java
│
├── 🌐 api-gateway/                  # Spring Cloud Gateway
│   ├── pom.xml
│   └── src/main/java/
│       └── com/example/demo/
│           ├── GatewayserviceApplication.java
│           └── filter/AuthFilter.java
│
├── 🔐 auth-service/                 # Authentication Service
│   ├── pom.xml
│   └── src/main/java/
│       └── com/example/auth/
│           ├── entities/            # AppUser, AppRole
│           ├── repo/                # JPA Repositories
│           ├── service/             # Business logic
│           ├── security/            # Security configuration
│           └── web/                 # REST Controllers
│
├── 👤 client-service/               # Client Management (MCP Server)
│   ├── pom.xml
│   └── src/main/java/
│       └── com/example/demo/
│           ├── entities/
│           ├── repository/
│           └── controller/
│
├── 📦 produit/                      # Product Management (MCP Server)
│   ├── pom.xml
│   └── src/main/java/
│       └── com/example/demo/
│           ├── produits/
│           ├── repository/
│           └── controller/
│
├── 🛒 commande-service/             # Order Management (MCP Server)
│   ├── pom.xml
│   └── src/main/java/
│       └── com/example/demo/
│           ├── entities/
│           ├── dto/
│           ├── feign/               # Feign clients
│           ├── model/
│           ├── repository/
│           └── controller/
│
├── 🤖 agent-ia-service/             # AI Agent (MCP Client)
│   ├── pom.xml
│   └── src/main/java/
│       └── com/example/agentia/
│           ├── config/              # MCP & AI configuration
│           └── controller/          # Chat endpoint
│
└── 🎨 frontend/                     # Angular Application
    ├── package.json
    ├── angular.json
    └── src/
        └── app/
```

---

## 🚀 Getting Started

### Prerequisites

Ensure you have the following installed:

- ✅ **Java 17** or higher
- ✅ **Maven 3.6+**
- ✅ **PostgreSQL** (running on `localhost:5432`)
- ✅ **Node.js 18+** and **npm** (for frontend)
- ✅ **Ollama** (for AI features, running on `localhost:11434`)

### 🗄️ Database Setup

```bash
# Connect to PostgreSQL and create required databases
psql -U postgres

CREATE DATABASE tpMicroservice;
\q
```

### 🔨 Build the Project

```bash
# Clone the repository
git clone https://github.com/amineouhiba26/MicroserviceSpringBoot.git
cd MicroserviceSpringBoot

# Build all services
mvn clean package -DskipTests
```

### 🏃 Run Services

**Option 1: Using the startup script**
```bash
chmod +x start-all.sh
./start-all.sh
```

**Option 2: Manual startup (in order)**

```bash
# 1. Start Discovery Service (Eureka)
java -jar discovery-service/target/discovery-service-0.0.1-SNAPSHOT.jar &

# Wait 10 seconds for Eureka to start
sleep 10

# 2. Start API Gateway
java -jar api-gateway/target/gateway-service-0.0.1-SNAPSHOT.jar &

# 3. Start Auth Service
java -jar auth-service/target/auth-service-0.0.1-SNAPSHOT.jar &

# 4. Start Client Service
java -jar client-service/target/client-service-0.0.1-SNAPSHOT.jar &

# 5. Start Product Service
java -jar produit/target/produit-service-0.0.1-SNAPSHOT.jar &

# 6. Start Order Service
java -jar commande-service/target/commande-service-0.0.1-SNAPSHOT.jar &

# 7. Start AI Agent Service (requires Ollama)
java -jar agent-ia-service/target/agent-ia-service-0.0.1-SNAPSHOT.jar &
```

### 🎨 Run Frontend

```bash
cd frontend
npm install
npm start
```

The frontend will be available at `http://localhost:4200`

### ✅ Verify Installation

1. **Eureka Dashboard**: http://localhost:8761
2. **API Gateway Health**: http://localhost:8888/actuator/health
3. **Client Service**: http://localhost:8082/clients
4. **Product Service**: http://localhost:9081/produits
5. **AI Chat**: http://localhost:8081/ask?message=Hello

---

## 📡 API Reference

### 🔐 Authentication

#### Register User
```bash
curl -X POST http://localhost:8888/auth-service/users \
  -H "Content-Type: application/json" \
  -d '{"username": "testuser", "password": "test123"}'
```

#### Login (Get JWT Token)
```bash
curl -X POST http://localhost:8888/auth-service/login \
  -H "Content-Type: application/json" \
  -d '{"username": "testuser", "password": "test123"}'
```

**Response:**
```json
{
  "access-token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### 📡 Authenticated Requests

Use the JWT token in the `Authorization` header:

```bash
# Get all clients
curl -H "Authorization: Bearer <YOUR_TOKEN>" \
  http://localhost:8888/client-service/clients

# Get all products
curl -H "Authorization: Bearer <YOUR_TOKEN>" \
  http://localhost:8888/produit-service/produits

# Get all orders
curl -H "Authorization: Bearer <YOUR_TOKEN>" \
  http://localhost:8888/commande-service/commandes
```

### 🤖 AI Chat

```bash
# Simple question
curl -G "http://localhost:8081/ask" \
  --data-urlencode "message=List all available products"

# Ask about clients (triggers MCP tools)
curl -G "http://localhost:8081/ask" \
  --data-urlencode "message=How many clients do we have?"
```

### 📊 API Endpoints Summary

| Endpoint | Method | Description | Auth Required |
|----------|--------|-------------|---------------|
| `/auth-service/users` | POST | Register new user | ❌ |
| `/auth-service/login` | POST | Get JWT token | ❌ |
| `/client-service/clients` | GET | List all clients | ✅ |
| `/client-service/clients/{id}` | GET | Get client by ID | ✅ |
| `/client-service/clients` | POST | Create new client | ✅ |
| `/produit-service/produits` | GET | List all products | ✅ |
| `/produit-service/produits/{id}` | GET | Get product by ID | ✅ |
| `/commande-service/commandes` | GET | List all orders | ✅ |
| `/agent-ia-service/ask` | GET | Chat with AI | ✅ |

---

## 🔧 Configuration

### Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5432/tpMicroservice` | Database connection URL |
| `SPRING_DATASOURCE_USERNAME` | `postgres` | Database username |
| `SPRING_DATASOURCE_PASSWORD` | `postgres` | Database password |
| `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE` | `http://localhost:8761/eureka/` | Eureka server URL |
| `OLLAMA_BASE_URL` | `http://localhost:11434` | Ollama API URL |

---

## 🛑 Stop Services

```bash
# Using the stop script
./stop-all.sh

# Or manually kill by port
lsof -ti:8761,8888,8080,8082,9081,9092,8081 | xargs kill -9
```

---

## 📚 Documentation

- 📖 [Project Documentation](./PROJECT_DOCUMENTATION.md) - Detailed technical documentation
- 🚀 [Run Guide](./RUN_GUIDE.md) - Step-by-step instructions for running and testing

---

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👤 Author

**Amine Ouhiba**

- GitHub: [@amineouhiba26](https://github.com/amineouhiba26)

---

<p align="center">
  Made with ❤️ using Spring Boot & Angular
</p>
