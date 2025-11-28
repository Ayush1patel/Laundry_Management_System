# Laundry App — Project README

## 🔷 Project Overview
This project handles student laundry orders, pricing, approval workflow, pickup, and persistent storage using JSON files with an in-memory cache.

The system supports:
- Students placing laundry orders
- Staff approving & managing orders
- Pricing calculations & quota handling
- Crash-safe atomic file persistence

---

## 🏗 High-level Architecture

| Layer | Responsibility |
|-------|----------------|
| **API Layer** | Controllers handling REST endpoints |
| **Service Layer** | Business logic (pricing, quota, states) |
| **Storage Layer** | File-backed persistence w/ caching |
| **Model Layer** | Domain models + API DTOs |
| **Support Layer** | Config, utilities, exceptions |
| **Tests** | Unit + integration test suite |

---

## 👥 Role-by-role Breakdown

---

### **Role 1 — Data Modeling Lead** - Abhyudaya Singh (BT2024180)
Responsible for the **shape and structure of all data**.

#### Responsibilities:
- Maintain all domain models (`Student`, `LaundryOrder`, etc.)
- Maintain all DTOs (request/response objects)
- Ensure data alignment between API and services

#### Files Owned:
- `model/` (all)
- `dto/` (all)

---

### **Role 2 — Data Persistence Lead** - Ayush Patel (BT2024171)
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

### **Role 3 — Core Business Logic Lead** - Utkarsh Rastogi (BT2024119)
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

### **Role 4 — API Lead (Student & General Endpoints)** - Vraj Vashi (BT2024062)
Owns all **student-facing** and **public** APIs.

#### Responsibilities:
- Develop `StudentController` (create/check orders)
- Maintain `OrderController` (pickup/status endpoints)
- Validate inputs and map DTO responses

#### Files Owned:
- `StudentController.java`
- `OrderController.java`

---

### **Role 5 — API Lead (Staff Endpoints)** - Ansh Rupavatia (IMT2024057)
Owns **staff admin console** operations.

#### Responsibilities:
- Build `StaffController` for approval, searching, queue handling
- Ensure endpoint security (auth strategy later)
- Provide admin filters and pagination

#### Files Owned:
- `StaffController.java`

---

### **Role 6 — Infrastructure & QA Lead** - Harshil Srivastava (BT2024187)
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

# Build the project
mvn clean install     # or ./gradlew build

# Run the application (Spring Boot example)
mvn spring-boot:run   # or ./gradlew bootRun
