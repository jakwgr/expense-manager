# Expense Manager API

A Spring Boot backend application for personal finance management, dynamic currency conversions using NBP (National Bank of Poland) rates, and automated multi-currency Excel report generation.

---

## Core Features

* **Transaction Tracking:** Manage expenses and incomes with ease.
* **NBP API Integration:** Real-time exchange rate fetching from the National Bank of Poland.
* **Excel Reports:** Auto-generated `.xlsx` reports featuring a detail table and a multi-currency summary converted into the user's base currency.

---

## Tech Stack

* **Language & Framework:** Java 21+, Spring Boot 3
* **Excel Library:** Apache POI (`poi-ooxml`)
* **Utilities:** Lombok, Jackson

---

## Getting Started

### Prerequisites

Ensure you have the following installed on your machine:
* **JDK 21** or higher
* **Apache Maven 3.8+**
* **Git**

### Installation & Execution

1. **Clone the repository:**
   git clone [https://github.com/jakwgr/expense-manager.git](https://github.com/jakwgr/expense-manager.git)
   cd expense-manager

2. **Build the project:**
Compile the code and package the application into a JAR file:
    mvn clean package


4. **Run the application:**
*Using Maven Spring Boot Plugin:*
    mvn spring-boot:run


*Or running the executable JAR directly:*
    java -jar target/expense-manager-0.0.1-SNAPSHOT.jar


5. **Access the API:**
The server will start at `http://localhost:8080`.

---

## TODO

* [ ] **Unit & Integration Testing:** Add comprehensive tests (JUnit 5 + Mockito) for `CurrencyExchangeService` and `OperationExcelService`.
* [ ] **Frontend Application:** Build a UI interface (Angular) to present dashboard summaries and download reports easily.