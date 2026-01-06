#!/bin/bash

echo "=============================================="
echo "  Starting Microservices Application"
echo "=============================================="

# Kill any existing Java processes
echo "Stopping any running services..."
pkill -f "java -jar" 2>/dev/null
sleep 2

cd /Users/amineouhiba/MicroserviceSpringBoot

# Start Discovery Service (Eureka)
echo "Starting Discovery Service (port 8761)..."
nohup java -jar discovery-service/target/discovery-service-0.0.1-SNAPSHOT.jar > discovery_log.txt 2>&1 &
sleep 5

# Start API Gateway
echo "Starting API Gateway (port 8888)..."
nohup java -jar api-gateway/target/gateway-service-0.0.1-SNAPSHOT.jar > api_gateway_log.txt 2>&1 &
sleep 3

# Start Auth Service
echo "Starting Auth Service (port 8080)..."
nohup java -jar auth-service/target/auth-service-0.0.1-SNAPSHOT.jar > auth_service_log.txt 2>&1 &
sleep 3

# Start Client Service
echo "Starting Client Service (port 8082)..."
nohup java -jar client-service/target/client-service-0.0.1-SNAPSHOT.jar > client_service_log.txt 2>&1 &
sleep 2

# Start Product Service
echo "Starting Product Service (port 9081)..."
nohup java -jar produit/target/produit-service-0.0.1-SNAPSHOT.jar > produit_log.txt 2>&1 &
sleep 2

# Start Commande Service
echo "Starting Commande Service (port 9092)..."
nohup java -jar commande-service/target/commande-service-0.0.1-SNAPSHOT.jar > commande_service_log.txt 2>&1 &
sleep 2

# Start Agent IA Service
echo "Starting Agent IA Service (port 8081)..."
nohup java -jar agent-ia-service/target/agent-ia-service-0.0.1-SNAPSHOT.jar > agent_ia_log.txt 2>&1 &
sleep 3

echo ""
echo "=============================================="
echo "  All backend services started!"
echo "=============================================="
echo ""
echo "Services:"
echo "  - Discovery:  http://localhost:8761"
echo "  - Gateway:    http://localhost:8888"
echo "  - Auth:       http://localhost:8080"
echo "  - Client:     http://localhost:8082"
echo "  - Product:    http://localhost:9081"
echo "  - Commande:   http://localhost:9092"
echo "  - Agent IA:   http://localhost:8081"
echo ""

# Start Frontend
echo "Starting Angular Frontend (port 4200)..."
cd /Users/amineouhiba/MicroserviceSpringBoot/frontend
nohup ng serve --open > ../frontend_log.txt 2>&1 &

echo ""
echo "=============================================="
echo "  Frontend starting at http://localhost:4200"
echo "=============================================="
echo ""
echo "Test credentials:"
echo "  Admin: amine / amine123"
echo "  User:  freshuser / fresh123"
echo ""
