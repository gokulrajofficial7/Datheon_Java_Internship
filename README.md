# Datheon Java Programming Internship

Production-ready, object-oriented Java solutions for the **Datheon Java Programming Internship (1 Month / 4 Weeks)**.

![Java Version](https://img.shields.io/badge/Java-JDK%2017%2B-ED8B00?logo=openjdk&logoColor=white)
![Build](https://img.shields.io/badge/Build-Passing-brightgreen)
![Tasks Completed](https://img.shields.io/badge/Tasks%20Completed-5%20of%205%20(100%25)-brightgreen)
![License](https://img.shields.io/badge/License-MIT-blue)

---

## 📋 Comprehensive Task Pack (All 5 Tasks Completed)

| Directory | Task Title | Difficulty | Key Concepts Demonstrated |
| :--- | :--- | :--- | :--- |
| [`Task1_StudentGradeManager/`](./Task1_StudentGradeManager/) | **Student Grade Management System** | Beginner | OOP Encapsulation, `Map` / `ArrayList`, Input Validation, Statistical Analytics (Average/Median/GPA), `String.format()` Tabular Reports. |
| [`Task2_ATM_Simulation/`](./Task2_ATM_Simulation/) | **Bank Account & ATM Simulation Engine** | Beginner | Abstract Classes, Inheritance, Runtime Polymorphism, Custom Exceptions (`InsufficientFundsException`, `InvalidPINException`), Audit Trail Logging. |
| [`Task3_WordCounter/`](./Task3_WordCounter/) | **Word Counter & Text Corpus Analyzer** | Intermediate | Java File I/O (`Files`, `PrintWriter`), Regular Expressions, `HashMap` Frequency Sorting, Lexical Diversity & Sentence Metrics. |
| [`Task4_LibraryManagement/`](./Task4_LibraryManagement/) | **Digital Library & Checkout Suite** | Intermediate | `java.time.LocalDate`, `ChronoUnit.DAYS`, Catalog Ingestion (`Map<String, Book>`), Automated Overdue Loan Penalties. |
| [`Task5_VehicleRental_SOLID/`](./Task5_VehicleRental_SOLID/) | **Vehicle Rental & Dispatch (SOLID)** | Intermediate | Strict SOLID Principles, Factory Design Pattern (`VehicleFactory`), Strategy Pattern (`PricingStrategy`), Java 8+ Streams Filtering. |

---

## 🏛️ Architecture & System Design Highlights

1. **Task 1: Student Grade Management System**
   - Encapsulation guarantees valid scores between 0.0 and 100.0.
   - Dynamic GPA 4.0 scaling and letter grade attribution (`A+` to `F`).
   - Automatically loads and formats records from `sample_student_grades.csv`.

2. **Task 2: Core Banking & ATM Simulation Engine**
   - Class hierarchy: `Account` (abstract) $\rightarrow$ `SavingsAccount` & `CheckingAccount`.
   - Custom exceptions safeguard operations: `InsufficientFundsException` and `InvalidPINException`.
   - Immutable audit logs capture timestamps, operation types, and resulting balances.

3. **Task 3: Word Counter & Text Corpus Analyzer**
   - High-throughput tokenization using regex boundaries and case folding.
   - Linguistic metrics: vocabulary richness rate, average sentence length, and word density.
   - Content categorization separating stop words from key technical terms.

4. **Task 4: Digital Library Inventory & Checkout Suite**
   - $O(1)$ ISBN-keyed catalog management via `Map<String, Book>`.
   - Date-driven checkout workflows calculating exact overdue days and late fees with `java.time.LocalDate`.
   - Ingests `books_catalog.csv` with multi-attribute search filters.

5. **Task 5: Vehicle Rental & Dispatch System (SOLID Principles)**
   - **Factory Pattern**: `VehicleFactory` abstracts concrete class instantiations (`Car`, `Truck`, `Motorcycle`).
   - **Strategy Pattern**: `PricingStrategy` decouples pricing algorithms (Standard, Volume Discount, Peak Demand Surge).
   - **Interface Segregation**: Fine-grained interfaces (`Rentable`, `Maintainable`).
   - **Java Streams**: Dynamic fleet querying by fuel type, price threshold, and availability.

---

## 🚀 Quick Start & Execution

```bash
# Task 1 (Student Grade Manager)
cd Task1_StudentGradeManager && javac Student.java GradeManager.java && java GradeManager

# Task 2 (ATM Simulator)
cd Task2_ATM_Simulation && javac *.java && java ATM

# Task 3 (Word Corpus Analyzer)
cd Task3_WordCounter && javac WordAnalyzer.java && java WordAnalyzer

# Task 4 (Digital Library Management)
cd Task4_LibraryManagement && javac *.java && java LibrarySystem

# Task 5 (Vehicle Rental & Dispatch System)
cd Task5_VehicleRental_SOLID && javac *.java && java VehicleRentalApp
```

---

## 👨‍💻 Verification & Submission
- **Developer:** Gokulraj A R
- **GitHub Repository:** [gokulrajofficial7/Datheon_Java_Internship](https://github.com/gokulrajofficial7/Datheon_Java_Internship)
- **Internship Organization:** [Datheon](https://www.datheon.in/)
- **Track:** Java Programming Internship (Full Stack Architecture)
