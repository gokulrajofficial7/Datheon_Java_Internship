# Task 4: Digital Library Inventory & Checkout Management System

## Overview
A comprehensive digital library administration suite managing book inventories, member loan checkouts, return processing, and automated overdue penalty calculations using modern Java Date/Time (`java.time.LocalDate`) and the Collections Framework.

---

## 🛠️ Key Technical Features
1. **Inventory Management & Collections:**
   - Fast $O(1)$ catalog lookup using `Map<String, Book>` indexed by ISBN.
   - Dynamic inventory tracking (`copiesAvailable` decremented upon checkout, restored upon return).
2. **Automated Due-Date & Overdue Penalty Engine:**
   - 14-day standard loan window computed automatically using `LocalDate.plusDays(14)`.
   - Days overdue calculated via `ChronoUnit.DAYS.between(dueDate, returnDate)`.
   - Automatic late fine assessment based on daily penalty rates (e.g. $1.50 / day).
3. **Multi-Criteria Search Filters:**
   - Case-insensitive search filtering across Title substrings, Author names, and Genres.
4. **Member & Loan Audit Tracking:**
   - Complete history of loans, returns, and accumulated member fines.

---

## 📁 Project Structure
```text
Task4_LibraryManagement/
├── books_catalog.csv         # Starter inventory dataset
├── Book.java                 # Book model with availability management
├── Member.java               # Library member entity
├── LoanRecord.java           # Checkout record & overdue fine calculation
├── LibrarySystem.java        # Main interactive console CLI application
└── README.md                 # Documentation
```

---

## 🚀 How to Compile & Run

### 1. Compile
```bash
javac *.java
```

### 2. Execute
```bash
java LibrarySystem
```

---

## 📊 Sample Output
```text
=========================================================================================================
                                DIGITAL LIBRARY INVENTORY CATALOG                                
=========================================================================================================
| ISBN            | Title                    | Author               | Genre            | Year | Copies  |
---------------------------------------------------------------------------------------------------------
| 978-0134685991  | Effective Java           | Joshua Bloch         | Technology       | 2018 | 4       |
| 978-0596009205  | Head First Java          | Kathy Sierra         | Technology       | 2005 | 2       |
| 978-0132350884  | Clean Code               | Robert C. Martin     | Software Engi... | 2008 | 5       |
| 978-0201633610  | Design Patterns          | Erich Gamma          | Computer Science | 1994 | 3       |
=========================================================================================================
```
