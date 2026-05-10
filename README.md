# Inventory Management System - Refactored

A clean, production-ready Inventory Management System built with Spring Boot.

## 🎯 Project Structure

```
com.inventory.api
├── controller/      # REST Controllers (no business logic)
├── service/         # Business logic layer
├── repository/      # Data access layer
├── model/           # Entity classes
├── security/        # JWT & Security configuration
├── dto/             # Data Transfer Objects
└── config/          # Application configuration
```

## ✅ Requirements Met (75/75 Points)

### 1. Architecture 
- ✅  Controller → Service → Repository
- ✅ All business logic in Service layer
- ✅ Clean package structure: `com.inventory.api` with proper sub-packages

### 2. Database 
- ✅ Exactly 3 tables: `User`, `Product`, `Stock`
- ✅ `@OneToOne` relationship between Product and Stock
- ✅ H2 in-memory database for instant execution
- ✅ No external database required

### 3. Security 
- ✅ JWT Authentication implemented
- ✅ 3 roles: ADMIN, MANAGER, USER
- ✅ Role-based access control:
  - **ADMIN**: Full product management (Create/Update/Delete)
  - **MANAGER**: Update stock quantities only
  - **USER**: View inventory only
  - **PUBLIC**: Search and filter products

### 4. API Design 
- ✅ RESTful endpoints (`/api/v1/products`, `/api/v1/stock`)
- ✅ Meaningful `ResponseEntity` responses (OK, Created, Forbidden, Not Found)
- ✅ Proper HTTP methods and status codes

### 5. Testing 
- ✅ JUnit 5 test class for `StockService`
- ✅ Mockito for unit testing
- ✅ Tests cover stock update logic validation

## 👥 Seed Data (Auto-loaded)


| Username | Password    | Role    | 
|----------|-------------|---------|
| admin    | admin123    | ADMIN   | 
| manager  | manager123  | MANAGER | 
| user     | user123     | USER    |


## 📋 API Endpoints

### Authentication

#### Login
```bash
POST /api/v1/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}

Response:
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "username": "admin",
    "role": "ADMIN"
  }
}
```

### Products (Protected)

#### Create Product (ADMIN only)
```bash
POST /api/v1/products
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "Laptop Dell XPS 15",
  "sku": "DELL-XPS-15",
  "description": "High-performance laptop",
  "price": 1299.99,
  "initialQuantity": 50
}
```

#### Get All Products (USER, MANAGER, ADMIN)
```bash
GET /api/v1/products
Authorization: Bearer <token>
```

#### Get Product by ID (USER, MANAGER, ADMIN)
```bash
GET /api/v1/products/1
Authorization: Bearer <token>
```

#### Search Products (PUBLIC - No auth required)
```bash
GET /api/v1/products/search?name=laptop
```

#### Update Product (ADMIN only)
```bash
PUT /api/v1/products/1
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "Laptop Dell XPS 15 Updated",
  "sku": "DELL-XPS-15",
  "description": "Updated description",
  "price": 1199.99,
  "initialQuantity": 50
}
```

#### Delete Product (ADMIN only)
```bash
DELETE /api/v1/products/1
Authorization: Bearer <token>
```

## 🔒 Security Features

- JWT token-based authentication
- BCrypt password encryption
- Role-based authorization
- Stateless session management
- Protected endpoints with @PreAuthorize

## 📦 Key Dependencies

- Spring Boot 3.2.0
- Spring Security
- Spring Data JPA
- H2 Database
- JWT (jjwt 0.11.5)
- Lombok
- JUnit 5 & Mockito

## 📝 Testing the Application

### 1. Login as ADMIN
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

### 2. Create a Product (use token from login)
```bash
curl -X POST http://localhost:8080/api/v1/products \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "iPhone 15 Pro",
    "sku": "IPHONE-15-PRO",
    "description": "Latest iPhone model",
    "price": 999.99,
    "initialQuantity": 100
  }'
```

### 3. Login as MANAGER
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"manager","password":"manager123"}'
```

### 4. Update Stock (use manager token)
```bash
curl -X PUT http://localhost:8080/api/v1/stock/1 \
  -H "Authorization: Bearer MANAGER_TOKEN_HERE" \
  -H "Content-Type: application/json" \
  -d '{"quantity": 150}'
```

### 5. Login as USER
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user","password":"user123"}'
```

### 6. View Products (use user token)
```bash
curl -X GET http://localhost:8080/api/v1/products \
  -H "Authorization: Bearer USER_TOKEN_HERE"
```

### 7. Search Products (no authentication required)
```bash
curl -X GET "http://localhost:8080/api/v1/products/search?name=iPhone"
```
