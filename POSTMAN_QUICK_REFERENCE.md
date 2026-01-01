# 📋 Quick Reference Card - Postman Collection

## 🚀 Quick Start (3 Steps)
1. **Import** → `Microservices_Complete_Collection.postman_collection.json`
2. **Login** → Run "Login - Get JWT Token" request
3. **Use** → Run any endpoint (token auto-applied)

## 🔑 Default Credentials
```
Username: admin
Password: 123
```

## 🌐 Base URL
```
http://localhost:8887
```
(All requests go through API Gateway)

## 📂 Collection Folders

| Folder | Endpoints | Auth | Role |
|--------|-----------|------|------|
| 1. Authentication | 1 | ❌ Public | - |
| 2. User Management | 4 | ✅ Required | ADMIN |
| 3. Client Management | 2 | ✅ Required | ADMIN |
| 4. Product Management | 3 | ✅ Required | ADMIN |
| 5. Order Management | 3 | ✅ Required | ADMIN |
| 6. Test & Validation | 4 | Mixed | - |

**Total: 17 endpoints**

## 🔐 Security Responses

| Status | Meaning | Cause |
|--------|---------|-------|
| 200 | ✅ Success | Authorized and successful |
| 401 | 🚫 Unauthorized | No/Invalid JWT token |
| 403 | ⛔ Forbidden | Valid token, but no ADMIN role |
| 404 | ❓ Not Found | Service down or wrong endpoint |
| 500 | 💥 Server Error | Service internal error |

## 📝 Common Request Bodies

### Login
```json
{
    "username": "admin",
    "password": "123"
}
```

### Create User
```json
{
    "username": "newuser",
    "password": "pass123"
}
```

### Create Role
```json
{
    "roleName": "USER"
}
```

### Assign Role
```json
{
    "username": "newuser",
    "roleName": "ADMIN"
}
```

### Create Product
```json
{
    "nom": "Product Name",
    "prix": 99.99
}
```

### Create Order
```json
{
    "idClient": 1,
    "productItems": [
        {"idProduit": 1, "quantite": 2},
        {"idProduit": 2, "quantite": 1}
    ]
}
```

## ⚡ Quick Tests

### Test 1: Get All Products (Protected)
```
GET /produit-service/produits
Header: Authorization: Bearer {{jwt_token}}
Expected: 200 OK (if ADMIN) or 403 (if not ADMIN)
```

### Test 2: Login (Public)
```
POST /auth-service/api/auth/login
Body: {"username":"admin","password":"123"}
Expected: 200 OK + JWT token
```

### Test 3: No Token (Should Fail)
```
GET /produit-service/produits
No Authorization header
Expected: 401 Unauthorized
```

## 🎯 Testing Workflow

```
1. Login (get token)
   ↓
2. Create entities (users, products, clients)
   ↓
3. View entities (get all, get by ID)
   ↓
4. Create orders (combine clients + products)
   ↓
5. View orders (see full details)
```

## 🛠️ Service Ports

| Service | Port | Required |
|---------|------|----------|
| Eureka | 8761 | ✅ Yes |
| Gateway | 8887 | ✅ Yes |
| Auth | 8084 | ✅ Yes |
| Client | 8081 | For client endpoints |
| Product | 8082 | For product endpoints |
| Order | 8083 | For order endpoints |

## 📊 Variables

| Variable | Default Value | Auto-Updated |
|----------|---------------|--------------|
| gateway_url | http://localhost:8887 | No |
| jwt_token | (empty) | ✅ Yes (on login) |

## 🔍 Troubleshooting One-Liners

| Problem | Quick Fix |
|---------|-----------|
| 401 on all requests | Run Login request first |
| 403 on protected endpoints | User needs ADMIN role |
| Connection refused | Start API Gateway (8887) |
| 404 on service | Check Eureka (8761) for registration |
| Token expired | Login again (24h expiry) |

## 🎓 Pro Tips

✅ **Check Console** - Login request logs token details  
✅ **Save Examples** - Save successful responses as examples  
✅ **Use Folders** - Organize by workflow, not service  
✅ **Environment Variables** - Create env for dev/prod  
✅ **Pre-request Scripts** - Add custom headers if needed  

## 📱 Postman Features Used

- ✅ Collection Variables (auto JWT management)
- ✅ Pre-request Scripts (none needed)
- ✅ Test Scripts (auto-save JWT on login)
- ✅ Folders (organized by domain)
- ✅ Descriptions (on every request)
- ✅ Examples (create your own!)

## 🎬 Demo Scenario

### Scenario: Create a complete order

```bash
# 1. Login
POST /auth-service/api/auth/login
→ Get JWT token (auto-saved)

# 2. View available clients
GET /client-service/clients
→ Note client ID (e.g., 1)

# 3. View available products
GET /produit-service/produits
→ Note product IDs (e.g., 1, 2)

# 4. Create order
POST /commande-service/commandes
Body: {
  "idClient": 1,
  "productItems": [
    {"idProduit": 1, "quantite": 2},
    {"idProduit": 2, "quantite": 1}
  ]
}
→ Order created!

# 5. View order details
GET /commande-service/commandes/1
→ See full order with client and product details
```

---

**Print this page for quick reference! 📄**
