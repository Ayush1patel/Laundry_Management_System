# Laundry Management System

A Spring Boot application for managing university laundry services, including order creation, approval workflows, and quota management.

## 🚀 How to Run

### 1. Prerequisites
- **Java 17+**
- **Maven** (optional, wrapper included)

### 2. Start the Application
Open a terminal in the project root and run:

**Windows:**
```powershell
.\mvnw.cmd spring-boot:run
```

**Mac/Linux:**
```bash
./mvnw spring-boot:run
```

The server will start at `http://localhost:8080`.

---

## 📚 API Endpoints

### 🎓 1. Student Endpoints
**Base URL:** `/api/student`

| Name | Method | URL | Description |
| :--- | :--- | :--- | :--- |
| **Create Order** | `POST` | `/order` | Create a new laundry order. |
| **Get Quota** | `GET` | `/{studentId}/quota` | Check remaining washes. |
| **Get Orders** | `GET` | `/{studentId}/orders` | View order history. |

**Create Order Body:**
```json
{
  "studentId": "student-1",
  "rollNumber": "123",
  "servicesRequested": ["WASH", "IRON"],
  "weightKg": 2.5,
  "itemsToIron": [
    {
      "clothes": { "type": "SHIRT", "ironCost": 8.0, "weightKg": 0.2 },
      "quantity": 2
    }
  ],
  "useQuota": true
}
```

### 👔 2. Staff Endpoints
**Base URL:** `/api/staff`

| Name | Method | URL | Description |
| :--- | :--- | :--- | :--- |
| **List Unapproved** | `GET` | `/unapproved` | View pending orders. |
| **Search Unapproved**| `GET` | `/unapproved/search?roll=123` | Find by roll number. |
| **Add Student** | `POST` | `/student` | Register a new student. |
| **Approve Order** | `POST` | `/approve` | Approve order & set pricing. |
| **List Queue** | `GET` | `/queue` | View active/approved orders. |

**Add Student Body:**
```json
{
  "name": "New Student",
  "email": "new@example.com",
  "rollNumber": "BT2024999",
  "quotaRemaining": 20,
  "semester": "1"
}
```

**Approve Order Body:**
```json
{
  "orderId": "ORD-XXXX",
  "collectedAmount": 50.0,
  "paymentMode": "CASH",
  "approvedBy": "Staff Name"
}
```

### 📦 3. Order Endpoints
**Base URL:** `/api/orders`

| Name | Method | URL | Description |
| :--- | :--- | :--- | :--- |
| **Get Details** | `GET` | `/{orderId}` | Get info for any order. |
| **Pickup** | `POST` | `/{orderId}/pickup` | Complete order with OTP. |

**Pickup Body:**
```json
{
  "otp": "1234"
}
```

---

## 👥 Role-by-role Breakdown (Roles 1–6)

### **Role 1 — Data Modeling Lead**
**Abhyudatya Singh (BT2024180)**
Responsible for the **shape and structure of all data**.

#### Responsibilities:
- Maintain all domain models (`Student`, `LaundryOrder`, etc.)
- Maintain all DTOs (request/response objects)
- Ensure data alignment between API and services

#### Files Owned:
- `model/` (all)
- `dto/` (all)

---

### **Role 2 — Data Persistence Lead**
**Ayush Patel (BT2024171)**
Owns file-based storage, caching, and safe I/O.

#### Responsibilities:
- Implement `StorageService` + `JsonFileStorageService`
- Manage atomic writes with `JsonRepository` and `AtomicFileWriter`
- Ensure thread safety and startup/shutdown persistence

#### Files Owned:
- `StorageService.java`
- `JsonFileStorageService.java`
- `storage/` directory (all)
- `data/` directory data files

---

### **Role 3 — Core Business Logic Lead**
**Utkarsh Rastogi (BT2024119)**
Implements **rules and processing** of orders.

#### Responsibilities:
- Implement order lifecycle (`create`, `approve`, `complete`)
- Implement pricing model & quota enforcement
- Manage student operations in `StudentService`

#### Files Owned:
- `OrderService.java`
- `PricingService.java`
- `StudentService.java`

---

### **Role 4 — API Lead (Student & General Endpoints)**
**Vraj Saurabhbhai Vashi (BT2024062)**
Owns all **student-facing** and **public** APIs.

#### Responsibilities:
- Develop `StudentController` (create/check orders)
- Maintain `OrderController` (pickup/status endpoints)
- Validate inputs and map DTO responses

#### Files Owned:
- `StudentController.java`
- `OrderController.java`

---

### **Role 5 — API Lead (Staff Endpoints)**
**Ansh Rupavatia (IMT2024057)**
Owns **staff admin console** operations.

#### Responsibilities:
- Build `StaffController` for approval, searching, queue handling
- Ensure endpoint security (auth strategy later)
- Provide admin filters and pagination

#### Files Owned:
- `StaffController.java`

---

### **Role 6 — Infrastructure & QA Lead**
**Harshil Srivastava (BT2024187)**
Maintains **app health**, config, and test coverage.

#### Responsibilities:
- Manage `config/`, `util/`, and custom exceptions
- Maintain `EtaService` & `OtpService`
- Own build pipeline: `pom.xml` / `build.gradle`
- Write unit + integration tests

#### Files Owned:
- `config/` directory
- `util/` directory
- `exception/` directory
- `EtaService.java`, `OtpService.java`
- All `test/` files
- Build script (`pom.xml` / `build.gradle`)
