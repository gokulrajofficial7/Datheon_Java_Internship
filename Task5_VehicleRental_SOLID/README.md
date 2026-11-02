# Task 5: OOPs Design Patterns: Vehicle Rental & Dispatch System (SOLID Principles)

## Overview
An enterprise-grade, modular vehicle dispatch and rental platform built strictly adhering to **SOLID Principles**, the **Factory Design Pattern**, the **Strategy Pattern**, and **Java 8+ Streams API**.

---

## 🏛️ SOLID Principles Architecture

| Principle | Implementation Details |
| :--- | :--- |
| **S - Single Responsibility** | Rate calculation logic is isolated within dedicated `PricingStrategy` implementations; maintenance telemetry is managed via `Maintainable`; and vehicle instantiation is decoupled into `VehicleFactory`. |
| **O - Open/Closed** | New vehicle models, types, or pricing models (e.g. promotional holiday discounts) can be introduced without modifying existing client code. |
| **L - Liskov Substitution** | Any subtype (`Car`, `Truck`, `Motorcycle`) can be substituted wherever the base abstraction `Vehicle` is required without altering program correctness. |
| **I - Interface Segregation** | Fine-grained interfaces (`Rentable`, `Maintainable`) prevent clients from depending on methods they do not use. |
| **D - Dependency Inversion** | High-level dispatch operations depend purely on abstractions (`Rentable`, `PricingStrategy`), not on concrete implementations. |

---

## 📐 Design Patterns

### 1. Creational: Factory Design Pattern (`VehicleFactory.java`)
Decouples client dispatch logic from concrete vehicle constructors:
```java
Vehicle car = VehicleFactory.createVehicle(VehicleType.CAR, "VH-101", "Tesla Model 3", FuelType.ELECTRIC, 85.00, 5);
```

### 2. Behavioral: Strategy Pattern (`PricingStrategy.java`)
Abstracts runtime pricing algorithms dynamically:
- `StandardPricingStrategy`: Flat linear rate.
- `LongTermDiscountStrategy`: 15% discount for 7+ days, 25% discount for 30+ days.
- `PeakSurgePricingStrategy`: 20% demand markup.

---

## 📁 Project Structure
```text
Task5_VehicleRental_SOLID/
├── Rentable.java                   # Segregated rental interface (ISP)
├── Maintainable.java               # Segregated maintenance interface (ISP)
├── VehicleType.java                # Enum classification
├── FuelType.java                   # Propulsion enum
├── PricingStrategy.java            # Strategy interface (SRP/OCP)
├── StandardPricingStrategy.java    # Flat pricing implementation
├── LongTermDiscountStrategy.java   # Volume discount pricing
├── PeakSurgePricingStrategy.java   # Surge pricing implementation
├── Vehicle.java                    # Abstract base vehicle (LSP)
├── Car.java                        # Concrete vehicle
├── Truck.java                      # Concrete vehicle
├── Motorcycle.java                 # Concrete vehicle
├── VehicleFactory.java             # Creational Factory Pattern
├── VehicleRentalApp.java           # Interactive CLI & Java Streams
└── README.md                       # Documentation
```

---

## 🚀 How to Compile & Run

### 1. Compile
```bash
javac *.java
```

### 2. Execute
```bash
java VehicleRentalApp
```

---

## 📊 Sample Output
```text
============================================================================================================
                                ENTERPRISE VEHICLE FLEET INVENTORY                               
============================================================================================================
| ID       | Type         | Model              | Fuel      | Daily Rate  | Status     | Specifications               |
------------------------------------------------------------------------------------------------------------
| VH-101   | CAR          | Tesla Model 3      | ELECTRIC  | $85.00      | AVAILABLE  | 5-Seater Passenger Sedan     |
| VH-102   | CAR          | Toyota Camry       | HYBRID    | $55.00      | AVAILABLE  | 5-Seater Passenger Sedan     |
| VH-201   | TRUCK        | Ford F-150         | DIESEL    | $95.00      | AVAILABLE  | 3.5 Ton Heavy Freight Payload |
| VH-202   | TRUCK        | Volvo FH16         | DIESEL    | $160.00     | AVAILABLE  | 10.0 Ton Heavy Freight Payload |
| VH-301   | MOTORCYCLE   | Yamaha MT-07       | PETROL    | $40.00      | AVAILABLE  | 689 cc Sport/Commuter Engine |
============================================================================================================
```
