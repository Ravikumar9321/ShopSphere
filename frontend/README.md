# 🛒 ShopSphere – Full Stack E-Commerce Platform

A scalable full-stack e-commerce application built with **Spring Boot 3.x** and **React 19**, featuring a stateless cart system, flexible order-payment workflow, and a modern cyber-neon UI.

---

## 📖 Project Overview

ShopSphere is designed with a **backend-driven architecture** and a responsive **React SPA frontend**.
The system supports a complete shopping lifecycle—from product discovery to order placement and payment tracking—while maintaining scalability and clean separation of concerns.

---

## 🚀 Backend Highlights

* Engineered **10+ RESTful APIs** for:

  * Product catalog
  * Cart management
  * Order processing
  * Payment workflows

* Designed **7 JPA entities**:

  * Cart, CartItem, Category, Order, OrderItem, Payment, Product

* Implemented **relational mappings**:

  * Product ↔ Category
  * Cart → CartItem → Product
  * Order → OrderItem → Product
  * Order → Payment

* Built a **stateless session-based cart** using `sessionId`:

  * Eliminates authentication dependency
  * Enables lightweight user tracking

* Developed a complete **order lifecycle pipeline**:

  * Cart → Order conversion
  * Item mapping & persistence
  * Payment lifecycle management
  * Order status tracking 

* Enabled **order-first, pay-later model**:

  * Users can place orders before payment
  * Supports **pending payment tracking**

* Ensured system reliability with:

  * Transactional boundaries
  * Cascading operations
  * Global exception handling
  * Input validation layers

---

## 🎨 Frontend Highlights

* Built a **4-page React SPA** using React Router v6:

  * Home → Products → Cart → Checkout → Order History

* Implemented **session-based cart UI**:

  * Users can retrieve cart using `sessionId`
  * Dynamic cart updates

* Developed complete **checkout experience**:

  * Order placement before payment
  * Payment handling with status updates

* Added **Order History dashboard**:

  * Displays all orders
  * Highlights **pending payments**

* Integrated **Axios** with:

  * API communication
  * Error handling
  * User feedback

* Applied performance optimizations:

  * React Hooks (`useState`, `useEffect`)
  * Memoization where required

* Enhanced UX with:

  * Real-time cart total calculation
  * Pending payment badges
  * Success confirmation modals

* Implemented **product filtering by category**:

  * Allows users to refine search
  * Improves product discoverability

---

## 🛠 Tech Stack

**Backend**

* Spring Boot
* JPA / Hibernate
* REST APIs

**Frontend**

* React 19
* React Router v7
* Axios

**Database**

* PostgreSQL

**Tools**

* Eclipse
* Git & GitHub

**UI/UX**

* CSS Grid & Flexbox
* Glassmorphism Design

---

## ⚙️ Installation

### 🔹 Backend Setup

```bash
git clone https://github.com/your-username/ShopSphere.git
```

* Import into Eclipse
* Configure PostgreSQL in `application.properties`
* Run Spring Boot application

### 🔹 Frontend Setup

```bash
cd frontend
npm install
npm start
```

---

## 📂 Application Flow

1. **Home Page** → Start shopping
2. **Products Page** → Browse & filter products by category
3. **Cart Page** → View cart using sessionId
4. **Checkout** → Place order before payment
5. **Order History** → Track orders & pending payments

---

## 🔮 Future Enhancements

* JWT-based authentication & authorization
* Admin dashboard for product & order management


---

