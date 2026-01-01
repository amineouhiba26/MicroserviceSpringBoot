# Postman Collection Guide

## 📦 Collection File
**File:** `Microservices_Complete_Collection.postman_collection.json`

## 🎯 Overview
This Postman collection contains **ALL** endpoints for the microservices project, with **every request going through the API Gateway** on port 8887.

## 📋 Collection Structure

### 1️⃣ Authentication (Public)
- **Login - Get JWT Token** - Authenticate and get JWT token (auto-saved)

### 2️⃣ User Management (ADMIN Required)
- Get All Users
- Create User
- Create Role
- Assign Role to User

### 3️⃣ Client Management (ADMIN Required)
- Get All Clients
- Get Client by ID

### 4️⃣ Product Management (ADMIN Required)
- Get All Products
- Get Product by ID
- Create Product

### 5️⃣ Order Management (ADMIN Required)
- Get All Orders
- Get Order by ID
- Create Order

### 6️⃣ Test & Validation
- Test - Access Without Token (401)
- Test - Access With Invalid Token (401)
- Test - Login Endpoint (Public)
- Discovery Service - Eureka

## 🚀 How to Use

### Step 1: Import the Collection
1. Open Postman
2. Click **Import** button
3. Select the file: `Microservices_Complete_Collection.postman_collection.json`
4. Click **Import**

### Step 2: Verify Variables
The collection comes with pre-configured variables:
- **gateway_url**: `http://localhost:8887` (API Gateway)
- **jwt_token**: (empty, will be auto-filled after login)

To check/edit:
1. Click on the collection name
2. Go to **Variables** tab
3. Verify `gateway_url` is correct for your setup

### Step 3: Authenticate
1. Open folder: **1. Authentication (Public)**
2. Run: **Login - Get JWT Token**
3. The JWT token will be **automatically saved** to the collection variable
4. You're now ready to use all protected endpoints!

### Step 4: Use Protected Endpoints
All protected endpoints will automatically use the saved JWT token via the `{{jwt_token}}` variable.

Just select any request and click **Send**!

## 🔐 Security Features

### Automatic Token Management
- Login request automatically saves the JWT token
- Token is decoded and logged to console
- All protected requests use the saved token automatically

### Protected Endpoints (Require ADMIN Role)
✅ All User Management endpoints
✅ All Client Management endpoints  
✅ All Product Management endpoints
✅ All Order Management endpoints

### Public Endpoints (No Auth Required)
✅ Login endpoint

## 📝 Request Examples

### Login Example
```json
POST http://localhost:8887/auth-service/api/auth/login

Body:
{
    "username": "admin",
    "password": "123"
}

Response:
eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJBRE1JTiJdLCJzdWIiOiJhZG1pbiIsImlhdCI6MTcwNDExMjE2MCwiZXhwIjoxNzA0MTk4NTYwfQ.xxx
```

### Create Product Example
```json
POST http://localhost:8887/produit-service/produits

Headers:
Authorization: Bearer {{jwt_token}}
Content-Type: application/json

Body:
{
    "nom": "New Laptop",
    "prix": 1299.99
}
```

### Create Order Example
```json
POST http://localhost:8887/commande-service/commandes

Headers:
Authorization: Bearer {{jwt_token}}
Content-Type: application/json

Body:
{
    "idClient": 1,
    "productItems": [
        {
            "idProduit": 1,
            "quantite": 2
        },
        {
            "idProduit": 2,
            "quantite": 1
        }
    ]
}
```

## 🧪 Testing Security

### Test 1: Unauthorized Access
Run: **Test - Access Without Token (401)**
- Expected: `401 Unauthorized`
- Validates: Gateway blocks unauthenticated requests

### Test 2: Invalid Token
Run: **Test - Access With Invalid Token (401)**
- Expected: `401 Unauthorized`
- Validates: Gateway validates JWT tokens

### Test 3: Non-ADMIN User
1. Create a user without ADMIN role
2. Login with that user
3. Try accessing protected endpoints
- Expected: `403 Forbidden`
- Validates: Gateway enforces ADMIN role requirement

## 📊 All Available Endpoints

| Method | Endpoint | Auth Required | Role Required | Description |
|--------|----------|---------------|---------------|-------------|
| POST | /auth-service/api/auth/login | ❌ No | - | Login and get JWT token |
| GET | /auth-service/api/auth/users | ✅ Yes | ADMIN | Get all users |
| POST | /auth-service/api/auth/users | ✅ Yes | ADMIN | Create new user |
| POST | /auth-service/api/auth/roles | ✅ Yes | ADMIN | Create new role |
| POST | /auth-service/api/auth/addRoleToUser | ✅ Yes | ADMIN | Assign role to user |
| GET | /client-service/clients | ✅ Yes | ADMIN | Get all clients |
| GET | /client-service/clients/{id} | ✅ Yes | ADMIN | Get client by ID |
| GET | /produit-service/produits | ✅ Yes | ADMIN | Get all products |
| GET | /produit-service/produits/{id} | ✅ Yes | ADMIN | Get product by ID |
| POST | /produit-service/produits | ✅ Yes | ADMIN | Create new product |
| GET | /commande-service/commandes | ✅ Yes | ADMIN | Get all orders |
| GET | /commande-service/commandes/{id} | ✅ Yes | ADMIN | Get order by ID |
| POST | /commande-service/commandes | ✅ Yes | ADMIN | Create new order |

## 🛠️ Prerequisites

Ensure all services are running:
1. ✅ Discovery Service (Eureka) - Port 8761
2. ✅ API Gateway - Port 8887
3. ✅ Auth Service - Port 8084
4. ✅ Client Service - Port 8081
5. ✅ Product Service - Port 8082
6. ✅ Commande Service - Port 8083

Check Eureka dashboard: http://localhost:8761

## 🔧 Troubleshooting

### Problem: All requests return 401
**Solution:** Make sure you've run the login request first to get a JWT token.

### Problem: All requests return 403
**Solution:** Your user doesn't have the ADMIN role. Use the admin account (username: admin, password: 123) or assign ADMIN role to your user.

### Problem: Connection refused
**Solution:** Make sure the API Gateway is running on port 8887.

### Problem: Service unavailable
**Solution:** 
1. Check if all services are registered in Eureka (http://localhost:8761)
2. Make sure the required service is running
3. Wait a few seconds for service registration to complete

### Problem: Token expired
**Solution:** Run the login request again to get a fresh token. Tokens expire after 24 hours by default.

## 💡 Tips

1. **Use Environments**: Create a Postman environment for different setups (dev, staging, prod)
2. **Check Console**: The login request logs decoded token info to the console
3. **Test Security**: Use the Test & Validation folder to verify security is working
4. **Monitor Services**: Keep the Eureka dashboard open to monitor service health
5. **Save Responses**: Use Postman's response saving feature to create examples

## 🎓 Learning Resources

### Understanding JWT Tokens
After login, check the Postman console to see the decoded JWT payload:
```json
{
  "roles": ["ADMIN"],
  "sub": "admin",
  "iat": 1704112160,
  "exp": 1704198560
}
```

### Understanding the Flow
1. Client → API Gateway (with JWT)
2. Gateway validates JWT and extracts roles
3. Gateway checks if endpoint requires ADMIN role
4. If authorized, Gateway forwards request to service
5. Service processes and returns response
6. Gateway returns response to client

## 📞 Support

For issues or questions:
1. Check the main README.md
2. Review SECURITY_IMPLEMENTATION.md
3. Check TESTING_GUIDE.md
4. Review the service logs

---

**Happy Testing! 🚀**
