# 🚀 Microservices Project - Complete Run & Test Guide

This guide provides step-by-step instructions for running and testing all microservices including the AI Agent with MCP integration.

---

## 📋 Prerequisites

Before starting, ensure you have:

- ✅ **Java 17** or higher installed
- ✅ **Maven 3.6+** installed
- ✅ **PostgreSQL** running on `localhost:5432`
- ✅ **Ollama** running on `localhost:11434` with `mistral:latest` model

### Verify Prerequisites

```bash
# Check Java version
java -version

# Check Maven version
mvn -version

# Check PostgreSQL is running
pg_isready -h localhost -p 5432

# Check Ollama is running
curl http://localhost:11434/api/tags
```

---

## 🗄️ Step 1: Database Setup

Create the required databases in PostgreSQL:

```sql
-- Connect to PostgreSQL
psql -U postgres

-- Create databases
CREATE DATABASE auth_db;
CREATE DATABASE clients;
CREATE DATABASE products;
CREATE DATABASE commandes;

-- Verify
\l
```

---

## 🔨 Step 2: Build All Services

From the project root directory:

```bash
cd /path/to/MicroserviceSpringBoot

# Build all services (skip tests for faster build)
mvn clean package -DskipTests
```

Expected output: `BUILD SUCCESS` for all modules.

---

## 🏃 Step 3: Start Services (In Order)

**Important:** Start services in this exact order!

### 3.1 Start Discovery Service (Eureka)
```bash
java -jar discovery-service/target/discovery-service-0.0.1-SNAPSHOT.jar &
```
⏳ Wait 10 seconds for Eureka to start.

✅ **Verify:** Open http://localhost:8761 - You should see the Eureka Dashboard.

---

### 3.2 Start API Gateway
```bash
java -jar api-gateway/target/gateway-service-0.0.1-SNAPSHOT.jar &
```
⏳ Wait 10 seconds.

✅ **Verify:** 
```bash
curl http://localhost:8888/actuator/health
```

---

### 3.3 Start Client Service (MCP Server)
```bash
java -jar client-service/target/client-service-0.0.1-SNAPSHOT.jar &
```
⏳ Wait 10 seconds.

✅ **Verify:**
```bash
curl http://localhost:8082/clients
```
Should return a list of clients.

---

### 3.4 Start Product Service (MCP Server)
```bash
java -jar produit/target/produit-service-0.0.1-SNAPSHOT.jar &
```
⏳ Wait 10 seconds.

✅ **Verify:**
```bash
curl http://localhost:9091/produits
```
Should return a list of products.

---

### 3.5 Start Auth Service
```bash
java -jar auth-service/target/auth-service-0.0.1-SNAPSHOT.jar &
```
⏳ Wait 10 seconds.

✅ **Verify:**
```bash
curl http://localhost:8080/users
```
Should return a list of users.

---

### 3.6 Start Commande Service
```bash
java -jar commande-service/target/commande-service-0.0.1-SNAPSHOT.jar &
```
⏳ Wait 10 seconds.

✅ **Verify:**
```bash
curl http://localhost:9092/commandes
```
Should return a list of commandes.

---

### 3.7 Start Agent IA Service (MCP Client)
```bash
java -jar agent-ia-service/target/agent-ia-service-0.0.1-SNAPSHOT.jar &
```
⏳ Wait 15 seconds (MCP connections need time to initialize).

✅ **Verify:**
```bash
curl -G "http://localhost:8081/ask" --data-urlencode "message=Bonjour"
```
Should return a greeting from the AI.

---

## ✅ Step 4: Verify All Services Are Running

### Check Eureka Dashboard
Open http://localhost:8761 and verify all services are registered:

| Service Name | Port | Status |
|--------------|------|--------|
| GATEWAYSERVICE | 8888 | ✅ UP |
| CLIENT-SERVICE | 8082 | ✅ UP |
| PRODUIT-SERVICE | 9091 | ✅ UP |
| AUTH-SERVICE | 8080 | ✅ UP |
| COMMANDE-SERVICE | 9092 | ✅ UP |
| AGENT-IA-SERVICE | 8081 | ✅ UP |

### Quick Health Check Script
```bash
echo "=== Service Health Check ==="
echo "Discovery: $(curl -s http://localhost:8761/actuator/health | grep -o '"status":"[^"]*"')"
echo "Gateway: $(curl -s http://localhost:8888/actuator/health | grep -o '"status":"[^"]*"')"
echo "Client: $(curl -s http://localhost:8082/clients >/dev/null && echo 'UP' || echo 'DOWN')"
echo "Product: $(curl -s http://localhost:9091/produits >/dev/null && echo 'UP' || echo 'DOWN')"
echo "Auth: $(curl -s http://localhost:8080/users >/dev/null && echo 'UP' || echo 'DOWN')"
echo "Commande: $(curl -s http://localhost:9092/commandes >/dev/null && echo 'UP' || echo 'DOWN')"
echo "Agent IA: $(curl -s 'http://localhost:8081/ask?message=test' >/dev/null && echo 'UP' || echo 'DOWN')"
```

---

## 🧪 Step 5: Test Each Service

### 5.1 Test Client Service

**Get all clients:**
```bash
curl http://localhost:8082/clients
```

**Get client by ID:**
```bash
curl http://localhost:8082/clients/1
```

**Create a new client:**
```bash
curl -X POST http://localhost:8082/clients \
  -H "Content-Type: application/json" \
  -d '{"name": "Test User", "email": "test@example.com"}'
```

---

### 5.2 Test Product Service

**Get all products:**
```bash
curl http://localhost:9091/produits
```

**Get product by ID:**
```bash
curl http://localhost:9091/produits/1
```

---

### 5.3 Test via API Gateway

The API Gateway requires JWT authentication. First get a token, then use it to access services.

**Step 1: Create a test user (only needed once):**
```bash
curl -X POST http://localhost:8888/auth-service/users \
  -H "Content-Type: application/json" \
  -d '{"username": "testuser", "password": "test123"}'
```

**Step 2: Get JWT Token:**
```bash
TOKEN=$(curl -s -X POST "http://localhost:8888/auth-service/login" \
  -d "username=testuser&password=test123" \
  -H "Content-Type: application/x-www-form-urlencoded" | \
  grep -o '"access-token":"[^"]*"' | cut -d'"' -f4)
echo "Token: $TOKEN"
```

**Step 3: Use token to access services via Gateway:**

**Get clients via Gateway:**
```bash
curl -H "Authorization: Bearer $TOKEN" http://localhost:8888/client-service/clients
```

**Get products via Gateway:**
```bash
curl -H "Authorization: Bearer $TOKEN" http://localhost:8888/produit-service/produits
```

**Get commandes via Gateway:**
```bash
curl -H "Authorization: Bearer $TOKEN" http://localhost:8888/commande-service/commandes
```

**Chat with AI Agent via Gateway:**
```bash
curl -G -H "Authorization: Bearer $TOKEN" \
  "http://localhost:8888/agent-ia-service/ask" \
  --data-urlencode "message=Bonjour"
```

---

### 5.4 Test Agent IA Service (AI Chat with MCP Tools)

**Simple greeting:**
```bash
curl -G "http://localhost:8081/ask" --data-urlencode "message=Bonjour, comment vas-tu?"
```

**Ask about clients (triggers MCP tool):**
```bash
curl -G "http://localhost:8081/ask" --data-urlencode "message=Combien de clients avons-nous?"
```

**Ask about products (triggers MCP tool):**
```bash
curl -G "http://localhost:8081/ask" --data-urlencode "message=Liste moi les produits disponibles"
```

**Test memory (ask follow-up):**
```bash
curl -G "http://localhost:8081/ask" --data-urlencode "message=Quel était mon dernier message?"
```

---

## 📊 Service Architecture

```
                    ┌─────────────────────────────────┐
                    │   Discovery Service (Eureka)    │
                    │         Port: 8761              │
                    └───────────────┬─────────────────┘
                                    │
                    ┌───────────────┼───────────────┐
                    │               │               │
        ┌───────────▼───────────┐   │   ┌───────────▼───────────┐
        │   API Gateway         │   │   │   Agent IA Service    │
        │   Port: 8888          │   │   │   Port: 8081          │
        └───────────┬───────────┘   │   │   (Ollama + MCP)      │
                    │               │   └───────────┬───────────┘
    ┌───────────────┼───────────────┤               │
    │               │               │               │ MCP Protocol
    ▼               ▼               ▼               ▼
┌───────┐     ┌───────┐     ┌───────┐     ┌───────────────┐
│Client │     │Product│     │ Auth  │     │   Commande    │
│ 8082  │     │ 9091  │     │ 8080  │     │    9092       │
│  MCP  │◄────┤  MCP  │     │       │     │               │
│Server │     │Server │     │       │     │               │
└───┬───┘     └───┬───┘     └───────┘     └───────────────┘
    │             │
    ▼             ▼
  PostgreSQL    PostgreSQL
  (clients)     (products)
```

---

## 🛑 Step 6: Stop All Services

```bash
# Kill all Java processes (careful if you have other Java apps running)
pkill -f "java -jar"

# Or kill specific ports
lsof -ti:8761 | xargs kill -9  # Discovery
lsof -ti:8888 | xargs kill -9  # Gateway
lsof -ti:8082 | xargs kill -9  # Client
lsof -ti:9091 | xargs kill -9  # Product
lsof -ti:8080 | xargs kill -9  # Auth
lsof -ti:9092 | xargs kill -9  # Commande
lsof -ti:8081 | xargs kill -9  # Agent IA
```

---

## 🐛 Troubleshooting

### Service won't start - Port already in use
```bash
lsof -ti:<PORT> | xargs kill -9
```

### Agent IA fails to connect to MCP servers
Make sure Client Service (8082) and Product Service (9091) are running BEFORE starting Agent IA.

### Database connection errors
Verify PostgreSQL is running and databases exist:
```bash
psql -U postgres -c "\l"
```

### Eureka not showing services
Wait 30 seconds after starting services - registration takes time.

### Ollama not responding
```bash
# Check if Ollama is running
curl http://localhost:11434/api/tags

# Pull the model if needed
ollama pull mistral:latest
```

---

## 📝 Quick Reference - All Endpoints

| Service | Direct URL | Via Gateway (with JWT) |
|---------|------------|------------------------|
| Eureka Dashboard | http://localhost:8761 | N/A |
| Auth - Users | http://localhost:8080/users | http://localhost:8888/auth-service/users |
| Auth - Login | http://localhost:8080/login | http://localhost:8888/auth-service/login |
| Clients | http://localhost:8082/clients | http://localhost:8888/client-service/clients |
| Products | http://localhost:9091/produits | http://localhost:8888/produit-service/produits |
| Commandes | http://localhost:9092/commandes | http://localhost:8888/commande-service/commandes |
| AI Chat | http://localhost:8081/ask?message=... | http://localhost:8888/agent-ia-service/ask?message=... |

**Note:** Gateway endpoints (except /auth-service/login and /auth-service/users POST) require JWT token in header:
```
Authorization: Bearer <your-jwt-token>
```

---

## 🎉 Success Criteria

You've successfully set up the project when:

1. ✅ All 7 services appear in Eureka Dashboard as UP (http://localhost:8761)
2. ✅ `curl http://localhost:8082/clients` returns client data
3. ✅ `curl http://localhost:9091/produits` returns product data
4. ✅ `curl http://localhost:8080/users` returns user data
5. ✅ `curl http://localhost:9092/commandes` returns commande data
6. ✅ AI Chat responds: `curl -G "http://localhost:8081/ask" --data-urlencode "message=Hello"`
7. ✅ Gateway auth works: Get token, then access services via gateway with Bearer token
