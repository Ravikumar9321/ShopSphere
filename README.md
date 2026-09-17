# 🛒 ShopSphere – Full Stack E-Commerce Platform

production‑ready full‑stack application for managing Organizers, Venues, Events, Attendees, and Registrations, built with Spring Boot + React.js, featuring secure JWT authentication, Swagger API documentation, and PostgreSQL database integration.

---

## 🚀 Tech Stack

### Backend

- Java 21
- Spring Boot 3.5.5
- Spring Security (JWT Authentication)
- Spring Data JPA / Hibernate
- PostgreSQL
- Swagger/OpenAPI 3.0
- Maven

### Frontend

- React.js
- Axios (with interceptors for JWT authentication)
- React Router
- useState & useEffect
- Responsive CSS

### Tools

- Git & GitHub
- Eclipse IDE

---

## 🏗️ Architecture

**Frontend (React)** ➝ **REST API (Spring Boot)** ➝ **PostgreSQL Database**

Backend follows a clean layered architecture:

- **Controller Layer** – Handles HTTP requests
- **Service Layer** – Business logic
- **Repository Layer** – Database interaction

---

## ✨ Features

📦 Product Management – Browse, filter, and view product catalog.

-🛒 Cart System – Stateless cart using sessionId, supports add/remove items.
-💳 Order & Payment Workflow – Place orders before payment, track pending payments.
-📋 Order History Dashboard – View all orders with payment status.
-🔍 Category Filtering – Refine product search by category.
-🔒 Validation & Reliability – Transactional boundaries, exception handling, input validation.
-📘 API Documentation – Swagger UI integration.

## 🗄️ Database Design

**Entities:**
-Product → id, name, description, price, stockQuantity, category
-Category → id, name, products (One‑to‑Many)
-Cart → id, sessionId, cartItems (One‑to‑Many)
-CartItem → id, quantity, product (Many‑to‑One), cart (Many‑to‑One)
-Order → id, date, status, orderItems (One‑to‑Many), payment (One‑to‑One)
-OrderItem → id, quantity, product (Many‑to‑One), order (Many‑to‑One)
-Payment → id, status, order (One‑to‑One)

---

## 📁 Project Structure

| Path          | Description                                                           |
| ------------- | --------------------------------------------------------------------- |
| `backend/`    | Spring Boot API                                                       |
| `entity/`     | Product, Category, Cart, CartItem, Order, OrderItem, Payment entities |
| `controller/` | REST Controllers                                                      |
| `service/`    | Business Logic                                                        |
| `repository/` | JPA Repositories                                                      |
| `frontend/`   | React Application                                                     |
| `management/` | ProductList, CartPage, Checkout, OrderHistory                         |
| `doc/`        | Screenshots & Documentation                                           |
| `README.md`   | This file                                                             |

---

## 🔗 REST API Endpoints

### Product APIs

| Method | Endpoint       | Description     |
| ------ | -------------- | --------------- |
| GET    | `/api/product` | Get all product |
| POST   | `/api/product` | Create product  |

### Cart APIs

| Method | Endpoint | Description |
| ------ | -------- | ----------- |

| GET | `/api/cart/sessionId/{sessionId}` | Get cart by sessionId |
| POST | `/api/cartitem/{sessionId}` | Add item to cart |

### Order APIs

| Method | Endpoint | Description |
| ------ | -------- | ----------- |

| POST | `/api/order/{sessionId}` | Place order from cart |
| GET | `/api/order/history/{sessionId}` | Get order history |

---

## 🧪 How to Run Locally

### 1️⃣ Configure PostgreSQL

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/shopsphereDB
spring.datasource.username=postgres
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
server.port=8080


 ###  2️⃣ Backend Setup (Eclipse)

-->   Import the backend project into Eclipse IDE
-->   Right‑click the project → Run As → Spring Boot App
-->    Backend runs on: http://localhost:8080

###  3️⃣ Frontend Setup
-->    cd frontend
-->   npm install
-->   npm start
```
