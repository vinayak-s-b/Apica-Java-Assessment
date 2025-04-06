# User Management & Journal Microservices System

This project implements two Spring Boot microservices:

- **User Service**: Handles user CRUD operations, authentication, role-based access control, and publishes user-related events to Kafka.
- **Journal Service**: Listens to Kafka events, logs them, and provides secure access to journaled user events.

---

##  Architecture Overview

```
+--------------+      +-------------+     +---------------------+
|              | ---> |             | --> |                     |
|  User Client |      | UserService |     |  Kafka (user-events)|
|              | <--- |(Spring Boot)| <-- |                     |
+--------------+      +-------------+     +---------------------+
                                         |
                                         v
                                 +---------------+
                                 | JournalService|
                                 | (Spring Boot) |
                                 +---------------+
```

---

## ⚙️ Technologies Used

- Java 17
- Spring Boot
- Spring Security (JWT Authentication)
- Kafka
- Docker & Docker Compose
- MySQL
- Postman

---

## 🧑‍💻 User Service

### Features

- Register a new user
- Login and JWT generation
- Get user details
- Update and delete user data
- Admin role to manage users
- Publishes events to Kafka `user-events` topic

### Endpoints

| Method | Endpoint                   | Description                    |
| ------ | -------------------------- | ------------------------------ |
| POST   | `/auth/login`              | User login, returns JWT        |
| POST   | `/users/register`          | Register new user              |
| GET    | `/users/getUserDetails`    | Get logged-in user details     |
| PUT    | `/users/UpdateUserDetails` | Update own user details        |
| GET    | `/users`                   | Get all users (ADMIN only)     |
| PUT    | `/users/updateUser/{id}`   | Update user by ID (ADMIN only) |
| DELETE | `/users/deleteUser/{id}`   | Delete user by ID (ADMIN only) |

---

## 🗂️ Journal Service

### Features

- Listens to `user-events` Kafka topic
- Stores and exposes user event logs
- Accessible to only ADMINs

### Endpoint

| Method | Endpoint  | Description                          |
| ------ | --------- | ------------------------------------ |
| GET    | `/events` | Get all user event logs (ADMIN only) |

---

## 🐳 Docker Compose

The `docker-compose.yml` includes images:

- vinayaksb24/user-service
- vinayaksb24/journal-service
- bitnami/kafka:4.0.0
- mysql:8

To run:

```bash
docker-compose up --build
```
---

## 🔐 **Security Flow**
- Users authenticate using username/password at /auth/login
- JWT token is issued and used in headers for all other requests
- Role (ADMIN or USER) is extracted from token and used for authorization.
- JWT token is validated on every request using a filter.
- Only ADMIN can access /users, /deleteUser/{id}, /updateUser/{id}, and /events.

---

## 📫 **How to Interact**
- Register User → /users/register
- Login → /auth/login to get JWT
- Use JWT in Authorization header as: Bearer <token>
- Access protected endpoints with token

---

## 📁 **Postman Collection**
- Import the provided Postman collection (UserManagementSystem.postman_collection.json) into Postman to test all available endpoints.

---

## 🚀 **Deployment**
Make sure Docker and Docker Compose are installed.
To deploy:

```bash
git clone https://github.com/vinayak-s-b/Apica-Java-Assessment.git
cd Apica-Java-Assessment
docker-compose up --build
```

---

## 📝 **Assumptions Made**
- Users register with username,email,password and a predefined role (ADMIN or USER)
- user-id is auto generated
- Only ADMINs can view or manage all users
- events has id(auto generated, eventMessage and timestamp
- One Kafka topic is used for journaling all user events

---




