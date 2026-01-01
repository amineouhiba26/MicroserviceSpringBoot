# Quick Test Guide - Gateway Security

## Prerequisites
Ensure all services are running:
1. Discovery Service (port 8761)
2. API Gateway (port 8887)
3. Auth Service (port 8084)
4. Client Service (port 8081)
5. Product Service (port 8082)
6. Commande Service (port 8083)

## Step 1: Setup ADMIN User (Do this ONCE)

### Option A: Direct to Auth Service (Bootstrap)
```bash
# Create ADMIN role
curl -X POST -H "Content-Type: application/json" \
  -d '{"roleName":"ADMIN"}' \
  http://localhost:8084/api/auth/roles

# Create admin user
curl -X POST -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}' \
  http://localhost:8084/api/auth/users

# Assign ADMIN role to user
curl -X POST -H "Content-Type: application/json" \
  -d '{"username":"admin","roleName":"ADMIN"}' \
  http://localhost:8084/api/auth/addRoleToUser
```

### Option B: Through Gateway (After initial setup via Option A)
Once you have an admin token, you can use the gateway for subsequent operations.

## Step 2: Login and Get JWT Token

```bash
curl -X POST -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}' \
  http://localhost:8887/auth-service/api/auth/login
```

**Expected Response**: A JWT token string like:
```
eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJBRE1JTiJdLCJzdWIiOiJhZG1pbiIsImlhdCI6MTcwNDExMjE2MCwiZXhwIjoxNzA0MTk4NTYwfQ.xxx
```

## Step 3: Test Protected Endpoints

Save the token from Step 2:
```bash
export TOKEN="<paste-your-token-here>"
```

### Test Product Endpoints (ADMIN Required)
```bash
# List all products
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8887/produit-service/produits

# Get specific product
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8887/produit-service/produits/1

# Create product
curl -X POST -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"nom":"Test Product","prix":99.99}' \
  http://localhost:8887/produit-service/produits
```

### Test Client Endpoints (ADMIN Required)
```bash
# List all clients
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8887/client-service/clients

# Get specific client
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8887/client-service/clients/1
```

### Test Order Endpoints (ADMIN Required)
```bash
# List all orders
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8887/commande-service/commandes

# Get specific order
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8887/commande-service/commandes/1

# Create order
curl -X POST -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "idClient": 1,
    "productItems": [
      {"idProduit": 1, "quantite": 2},
      {"idProduit": 2, "quantite": 1}
    ]
  }' \
  http://localhost:8887/commande-service/commandes
```

### Test User Management (ADMIN Required)
```bash
# List all users
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8887/auth-service/api/auth/users

# Create new user
curl -X POST -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"username":"newuser","password":"pass123"}' \
  http://localhost:8887/auth-service/api/auth/users

# Create new role
curl -X POST -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"roleName":"USER"}' \
  http://localhost:8887/auth-service/api/auth/roles

# Add role to user
curl -X POST -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"username":"newuser","roleName":"USER"}' \
  http://localhost:8887/auth-service/api/auth/addRoleToUser
```

## Step 4: Test Unauthorized Access (Should Fail)

### Without Token (401 Expected)
```bash
curl -v http://localhost:8887/produit-service/produits
# Expected: HTTP 401 Unauthorized

curl -v http://localhost:8887/client-service/clients
# Expected: HTTP 401 Unauthorized

curl -v http://localhost:8887/commande-service/commandes
# Expected: HTTP 401 Unauthorized
```

### With Invalid Token (401 Expected)
```bash
curl -v -H "Authorization: Bearer invalid-token-here" \
  http://localhost:8887/produit-service/produits
# Expected: HTTP 401 Unauthorized
```

### With Valid Token but Without ADMIN Role (403 Expected)
First create a non-admin user and get their token:
```bash
# Create USER role (as admin)
curl -X POST -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"roleName":"USER"}' \
  http://localhost:8887/auth-service/api/auth/roles

# Create regular user (as admin)
curl -X POST -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"username":"regular","password":"pass123"}' \
  http://localhost:8887/auth-service/api/auth/users

# Assign USER role (not ADMIN)
curl -X POST -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"username":"regular","roleName":"USER"}' \
  http://localhost:8887/auth-service/api/auth/addRoleToUser

# Login as regular user
REGULAR_TOKEN=$(curl -X POST -H "Content-Type: application/json" \
  -d '{"username":"regular","password":"pass123"}' \
  http://localhost:8887/auth-service/api/auth/login)

# Try to access protected endpoint (should get 403)
curl -v -H "Authorization: Bearer $REGULAR_TOKEN" \
  http://localhost:8887/produit-service/produits
# Expected: HTTP 403 Forbidden
```

## Expected Outcomes Summary

| Scenario | Endpoint | Expected Status |
|----------|----------|----------------|
| No token | Any protected endpoint | 401 Unauthorized |
| Invalid token | Any protected endpoint | 401 Unauthorized |
| Valid token, no ADMIN role | Product/Client/Order endpoints | 403 Forbidden |
| Valid token, has ADMIN role | Product/Client/Order endpoints | 200 OK (with data) |
| Any request | /api/auth/login | 200 OK (public) |

## Troubleshooting

### Problem: Getting 401 even with valid token
- Check that token hasn't expired (default: 24 hours)
- Verify token format: must start with "Bearer "
- Check that JWT secret matches between gateway and auth service

### Problem: Getting 403 with admin user
- Verify user has ADMIN role: `curl -H "Authorization: Bearer $TOKEN" http://localhost:8887/auth-service/api/auth/users`
- Check that role name is exactly "ADMIN" (case-sensitive)

### Problem: Public endpoint returning 401
- Verify the path is in the PUBLIC_PATHS list in JwtAuthenticationFilter
- Check gateway logs for path matching issues

### Problem: Services not accessible
- Ensure all services are registered with Eureka: http://localhost:8761
- Check that service names match in the filter (e.g., "produit-service", not "PRODUIT-SERVICE")
