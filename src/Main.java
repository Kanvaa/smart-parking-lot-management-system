import db.DatabaseManager;
import exception.DuplicateEntryException;
import exception.InvalidExitException;
import exception.ParkingFullException;
import model.Bike;
import model.Car;
import model.Truck;
import model.Vehicle;
import service.ParkingLot;
import service.Receipt;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DatabaseManager dbManager = new DatabaseManager();
        // Small lot for demo: 3 bike slots, 2 car slots, 1 truck slot
        ParkingLot lot = new ParkingLot(3, 2, 1, dbManager);
        Scanner sc = new Scanner(System.in);

        boolean running = true;
        while (running) {
            printMenu();
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1" -> handleEntry(sc, lot);
                case "2" -> handleExit(sc, lot);
                case "3" -> lot.printStatus();
                case "4" -> dbManager.printAllTransactions();
                case "5" -> System.out.printf("Total Revenue: Rs. %.2f%n", dbManager.getTotalRevenue());
                case "6" -> {
                    running = false;
                    dbManager.close();
                    System.out.println("Exiting. Goodbye!");
                }
                default -> System.out.println("Invalid choice, try again.");
            }
        }
        sc.close();
    }

    private static void printMenu() {
        System.out.println("\n===== SMART PARKING LOT MANAGEMENT SYSTEM =====");
        System.out.println("1. Park a vehicle");
        System.out.println("2. Remove a vehicle (exit + billing)");
        System.out.println("3. View lot status");
        System.out.println("4. View transaction history");
        System.out.println("5. View total revenue");
        System.out.println("6. Exit");
        System.out.print("Choose an option: ");
    }

    private static void handleEntry(Scanner sc, ParkingLot lot) {
        System.out.print("Enter vehicle type (BIKE/CAR/TRUCK): ");
        String type = sc.nextLine().trim().toUpperCase();
        System.out.print("Enter vehicle number: ");
        String number = sc.nextLine().trim();

        Vehicle vehicle;
        switch (type) {
            case "BIKE" -> vehicle = new Bike(number);
            case "CAR" -> vehicle = new Car(number);
            case "TRUCK" -> vehicle = new Truck(number);
            default -> {
                System.out.println("Unknown vehicle type.");
                return;
            }
        }

        try {
            lot.parkVehicle(vehicle);
        } catch (ParkingFullException | DuplicateEntryException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void handleExit(Scanner sc, ParkingLot lot) {
        System.out.print("Enter vehicle number to exit: ");
        String number = sc.nextLine().trim();
        try {
            Receipt receipt = lot.removeVehicle(number);
            receipt.print();
        } catch (InvalidExitException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
