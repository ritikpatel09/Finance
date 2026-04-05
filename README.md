# 💰 Finance Data Processing & Access Control Backend

##  Overview

This project is a backend system for managing financial records with **role-based access control**.
It is designed to demonstrate clean backend architecture, secure authentication, and scalable API design.

The system allows different users (Admin, Analyst, Viewer) to interact with financial data based on their permissions.

---

##  Tech Stack

* **Java 17**
* **Spring Boot**
* **Spring Security (JWT)**
* **Hibernate / JPA**
* **MySQL**
* **Lombok**

---

##  Authentication & Security

* JWT-based authentication
* Stateless session management
* Custom login token stored in database (`TokenAnalysis`)
* Role-based authorization handled in service layer

##  Rate Limiting (Bucket4j + Caffeine)

To protect APIs from abuse and brute-force attacks, rate limiting is implemented using:

* Bucket4j → Controls request rate
* Caffeine Cache → Stores IP-based buckets with auto-expiry
##  How It Works
Each request is identified using client IP address
A Bucket is assigned per IP
Each request consumes 1 token
If tokens are exhausted → request is blocked

###  Flow

1. User logs in → JWT token generated
2. Token contains `loginToken`
3. `loginToken` is stored in DB with user mapping
4. Every request:

    * Token is validated
    * User is fetched via `loginToken`
    * Role & account status checked

---

## 👥 Roles & Permissions

| Role    | Permissions                       |
| ------- | --------------------------------- |
| ADMIN   | Full access (CRUD + manage users) |
| ANALYST | View all records                  |
| VIEWER  | View own records                  |

---

## 📂 Features

###  User Management

* Register user
* Login user
* Admin can update user roles

---

###  Financial Records

* Create record (Admin only)
* Update record (Admin)
* Delete record (Admin)
* Get own records (All roles with restriction)
* Get all records (Admin & Analyst)

---

###  Pagination

* Implemented using `Pageable`
* Fixed page size: **10 records**
* Supports page-based navigation

---

## 📡 API Endpoints

### 🔐 Authentication

#### Register

```http
POST /auth/register
```

```json
{
  "email": "user@gmail.com",
  "password": "123456"
}
```

---

#### Login

```http
POST /auth/login
```

```json
{
  "email": "user@gmail.com",
  "password": "123456"
}
```

---

### 💰 Financial APIs

#### Create Record (Admin Only)

```http
POST /records/createRecord
```

```json
{
  "userId": 2,
  "amount": 5000,
  "type": "INCOME",
  "category": "SALARY",
  "description": "Monthly salary"
}
```

---

#### Get User Records (Pagination)

```http
GET /records/getRecord/{page}
```

---

#### Get All Records (Admin & Analyst)

```http
GET /records/all/{page}
```

---

#### Update Record

```http
PUT /records/update/{id}
```

```json
{
  "amount": 200,
  "type": "EXPENSE",
  "category": "FOOD",
  "description": "Lunch"
}
```

---

#### Delete Record

```http
DELETE /records/delete/{id}
```

---

### 👤 Role Management

#### Update User Role (Admin Only)

```http
PUT /records/updateRole
```

```json
{
  "userId": 2,
  "role": "ANALYST"
}
```

---

## 🔐 Headers (Required for Protected APIs)

```http
Authorization: Bearer <JWT_TOKEN>
```

---

##  Response Format

All APIs return a standardized response:

```json
{
  "message": "Success message",
  "status": 1,
  "code": "200",
  "data": {}
}
```

---

##  Error Handling

* 401 → Unauthorized (invalid/missing token)
* 403 → Access Denied (role issue)
* 400 → Bad request / validation error

---

##  Design Decisions

* **Service Layer Role Check** instead of annotations
* **DTO-based architecture** (no entity exposure)
* **TokenAnalysis table** for session tracking
* **Pagination for scalability**
* **Centralized response structure**

---

##  Key Highlights

* Secure JWT implementation with DB session validation
* Clean separation of concerns (Controller → Service → Repository)
* Role-based access without using `@PreAuthorize`
* Scalable pagination design
* Production-style error handling

---

##  Future Improvements

* Global exception handler can be better
* Filtering + sorting APIs
* Dashboard analytics (income vs expense)
* Soft delete support
* I created Jwt Authentication but that one use query again and again that can be improve.
* For Role based i should use Enum class
* Unit & integration testing

---

## ‍ Author

**Ritik Patel**

---
