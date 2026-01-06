#!/bin/bash

echo "=============================================="
echo "  Stopping All Services"
echo "=============================================="

# Stop Java services
echo "Stopping backend services..."
pkill -f "java -jar" 2>/dev/null

# Stop Angular
echo "Stopping Angular frontend..."
pkill -f "ng serve" 2>/dev/null

echo ""
echo "All services stopped."
