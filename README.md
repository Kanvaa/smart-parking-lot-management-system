# Smart Parking Lot Management System

A console-based Java application that manages real-time parking slot allocation, vehicle entry/exit, and automatic fee calculation for bikes, cars, and trucks — built with plain Core Java (no frameworks).

## Features
- **OOP design**: abstract `Vehicle` class with `Bike`, `Car`, and `Truck` subclasses demonstrating inheritance and polymorphism (each type has its own hourly rate).
- **Collections Framework**: `HashMap` for O(1) active vehicle lookup, `EnumMap` for per-type slot capacity/occupancy tracking.
- **Custom exceptions**: `ParkingFullException`, `InvalidExitException`, `DuplicateEntryException` for predictable error handling.
- **Persistence**: SQLite database via JDBC — every transaction (entry, exit, fee) is saved and survives restarts. Revenue reports are calculated straight from the database.
- **Receipt generation** on exit, with entry/exit timestamps and computed fee.

## Tech Stack
Java (Core), OOP, Collections Framework, JDBC, SQLite, Eclipse IDE

## Project Structure
```
src/
  Main.java                  - console menu / entry point
  model/
    Vehicle.java              - abstract base class
    Bike.java, Car.java, Truck.java
    VehicleType.java          - enum
  exception/
    ParkingFullException.java
    InvalidExitException.java
    DuplicateEntryException.java
  service/
    ParkingLot.java           - core parking logic
    Receipt.java              - receipt generation
  db/
    DatabaseManager.java      - JDBC/SQLite persistence
```

## How to Run

### Requirements
- JDK 17+
- SQLite JDBC driver (`sqlite-jdbc-3.45.1.0.jar` or similar) — download from [Maven Central](https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/)

### In Eclipse
1. Import the project (File → Open Projects from File System).
2. Right-click project → Build Path → Add External JARs → select the downloaded `sqlite-jdbc-*.jar`.
3. Run `Main.java`.

### From command line
```
javac -d bin -cp lib/sqlite-jdbc-3.45.1.0.jar $(find src -name "*.java")
java -cp "bin:lib/sqlite-jdbc-3.45.1.0.jar" Main
```
(On Windows, use `;` instead of `:` in the classpath.)

A `parking_lot.db` SQLite file will be created automatically in the project root on first run.

## Sample Menu
```
1. Park a vehicle
2. Remove a vehicle (exit + billing)
3. View lot status
4. View transaction history
5. View total revenue
6. Exit
```


AUTHOR

KANVADITHYA GANAPATHI TIGULLA
LINKEDIN URL: https://linkedin.com/in/kanvadithya-ganapathi-tigulla/
