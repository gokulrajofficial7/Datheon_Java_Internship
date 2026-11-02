# Task 1: Student Grade Management System (OOP)

## Overview
A robust object-oriented Java console application designed to manage student academic profiles, record course scores, compute statistical metrics (Mean, Median, GPA), and render formatted academic report cards.

---

## 🛠️ Key Technical Features & OOP Principles
- **Encapsulation & Data Validation**: 
  - `Student.java` uses private attributes (`studentId`, `name`, `grades`).
  - Strict validation guarantees student ID is positive and course scores remain within `0.0` to `100.0`.
- **Java Collections Framework**:
  - `Map<String, Double>` (LinkedHashMap) maps course titles to numerical scores preserving insertion order.
  - `ArrayList<Student>` maintains the dynamic cohort list in `GradeManager.java`.
- **Statistical Analytics**:
  - Automatically computes Class Average and Median percentage.
  - Identifies Top Performer with Grade & 4.0 GPA conversion.
- **Automated CSV Ingestion**:
  - Reads standard `sample_student_grades.csv` into memory via `BufferedReader`.
- **Formatted Tabular Output**:
  - Formats clean, dynamic academic report cards using `String.format()`.

---

## 📁 Project Structure
```text
Task1_StudentGradeManager/
├── sample_student_grades.csv   # Starter dataset
├── Student.java                # Student OOP model with encapsulation
├── GradeManager.java           # Main execution class, CLI menu & analytics
└── README.md                   # Documentation
```

---

## 🚀 How to Compile & Run

### 1. Compile
```bash
javac Student.java GradeManager.java
```

### 2. Execute
```bash
java GradeManager
```

---

## 📊 Sample Output
```text
=========================================================================================================
                       OFFICIAL ACADEMIC GRADE REPORT SHEET                             
=========================================================================================================
------------------------------------------------------------------------------------------------------------------
| ID     | Student Name     | Math      | Science   | English   | Computer  | Total   | Avg(%)   | GPA   | Grade |
------------------------------------------------------------------------------------------------------------------
| 101    | Aarav Sharma     | 88.0      | 92.0      | 79.0      | 95.0      | 354.0   | 88.50    | 3.50  | A     |
| 102    | Diya Patel       | 95.0      | 89.0      | 94.0      | 98.0      | 376.0   | 94.00    | 4.00  | A+    |
| 103    | Rohan Gupta      | 72.0      | 68.0      | 75.0      | 80.0      | 295.0   | 73.75    | 3.00  | B     |
| 104    | Ananya Iyer      | 91.0      | 96.0      | 88.0      | 94.0      | 369.0   | 92.25    | 4.00  | A+    |
| 105    | Vikram Singh     | 65.0      | 70.0      | 72.0      | 68.0      | 275.0   | 68.75    | 2.50  | C     |
------------------------------------------------------------------------------------------------------------------
```
