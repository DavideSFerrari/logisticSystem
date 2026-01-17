# Logistics System Simulation (Java SE)

## 1. Application Overview
A secure, multi-threaded Java application simulating a port logistics network. It manages the lifecycle of cargo containers moving between Ships, Terminals, and Warehouses using real-time daemon threads to prevent capacity overflows.
...


## 2. Technologies & Patterns (Score Justification)

### Design Patterns (Minimum & Maximum Requirements)
- **Singleton (2 pts):** `ContainerRegister` ensures a single point of truth for container tracking.
- **Factory (3 pts):** `ContainerFactory` handles the creation of `Box` and `HighCube` variants.
- **Abstract Factory (3 pts):** `Warehouse` interface creates families of Import/Export facilities (Clothing, Food, etc.).
- **Composite (4 pts):** `Terminal` structure treats single terminals and complex hubs uniformly.
- **Iterator (4 pts):** `ContainerIterator` allows safe traversal of collections without exposing internal lists.
- **Exception Shielding (5 pts):** `ExceptionShieldingHandler` sanitizes errors, preventing stack traces from reaching the UI.
...

### Core Technologies
- **Multithreading (8 pts):** `WarehouseTruck` runs as a Daemon Thread with `synchronized` blocks to safely modify terminal state during user operations.
- **Reflection (4 pts):** Used in `PatternScanner` to auto-generate system reports and inject `@CapacityLimit` configurations at runtime.
- **Java I/O (3 pts):** The system generates a persistent `system_report.txt` file using `PrintWriter` and `Try-With-Resources`.
- **Annotations (2 pts):** Custom `@AppAuthor`, `@AppDesignPattern` and `@CapacityLimit` tags for metadata.
...

## 3. Design Decisions
- **Thread Safety:** I chose **Explicit Synchronization** (`synchronized` blocks) over concurrent collections to demonstrate precise control over critical sections (Truck vs Ship access).
- **Streams vs Iterators:** I prioritized **Iterators** for the core logic to ensure transactional safety during modification, while using **Reflection** for advanced data processing.
...

## 4. Security Measures
- **Input Sanitization:** All menu inputs are parsed via `getSafeIntInput` to prevent crashes.
- **No Hardcoded Secrets:** Configuration is injected via annotations, not hardcoded strings.
- **Controlled Propagation:** Exceptions are caught, logged securely via `Logger`, and presented as user-friendly messages.
...

## 5. Known Limitations and Future Work
- **Testing:** A formal JUnit suite is replaced by the `PatternScanner` integrity check, which validates architecture compliance at startup.
...