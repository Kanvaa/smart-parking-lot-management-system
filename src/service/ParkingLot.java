package service;

import db.DatabaseManager;
import exception.DuplicateEntryException;
import exception.InvalidExitException;
import exception.ParkingFullException;
import model.Vehicle;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import model.VehicleType;

public class ParkingLot {
    // Max slots allowed per vehicle type
    private final Map<VehicleType, Integer> capacity = new EnumMap<>(VehicleType.class);
    // Currently parked vehicles, keyed by vehicle number for O(1) lookup
    private final Map<String, Vehicle> activeVehicles = new HashMap<>();
    // Count of currently occupied slots per type
    private final Map<VehicleType, Integer> occupied = new EnumMap<>(VehicleType.class);

    private final DatabaseManager dbManager;

    public ParkingLot(int bikeSlots, int carSlots, int truckSlots, DatabaseManager dbManager) {
        capacity.put(VehicleType.BIKE, bikeSlots);
        capacity.put(VehicleType.CAR, carSlots);
        capacity.put(VehicleType.TRUCK, truckSlots);

        for (VehicleType type : VehicleType.values()) {
            occupied.put(type, 0);
        }
        this.dbManager = dbManager;
    }

    public void parkVehicle(Vehicle vehicle) throws ParkingFullException, DuplicateEntryException {
        if (activeVehicles.containsKey(vehicle.getVehicleNumber())) {
            throw new DuplicateEntryException(
                    "Vehicle " + vehicle.getVehicleNumber() + " is already parked.");
        }

        VehicleType type = vehicle.getType();
        int used = occupied.get(type);
        int max = capacity.get(type);

        if (used >= max) {
            throw new ParkingFullException(
                    "No available slots for " + type + ". Lot is full.");
        }

        activeVehicles.put(vehicle.getVehicleNumber(), vehicle);
        occupied.put(type, used + 1);

        System.out.println(vehicle + " parked successfully. Slots left for "
                + type + ": " + (max - used - 1));
    }

    public Receipt removeVehicle(String vehicleNumber) throws InvalidExitException {
        Vehicle vehicle = activeVehicles.get(vehicleNumber);
        if (vehicle == null) {
            throw new InvalidExitException(
                    "No active entry found for vehicle: " + vehicleNumber);
        }

        LocalDateTime exitTime = LocalDateTime.now();
        double amount = calculateFee(vehicle, exitTime);

        activeVehicles.remove(vehicleNumber);
        VehicleType type = vehicle.getType();
        occupied.put(type, occupied.get(type) - 1);

        Receipt receipt = new Receipt(
                vehicle.getVehicleNumber(),
                type.toString(),
                vehicle.getEntryTime(),
                exitTime,
                amount
        );

        // Persist transaction to SQLite via JDBC
        dbManager.saveTransaction(
                vehicle.getVehicleNumber(),
                type.toString(),
                vehicle.getEntryTime(),
                exitTime,
                amount
        );

        return receipt;
    }

    private double calculateFee(Vehicle vehicle, LocalDateTime exitTime) {
        Duration duration = Duration.between(vehicle.getEntryTime(), exitTime);
        long minutes = duration.toMinutes();
        // Minimum billing of 1 hour, then charged per full/partial hour
        long hours = Math.max(1, (minutes + 59) / 60);
        return hours * vehicle.getHourlyRate();
    }

    public void printStatus() {
        System.out.println("---- CURRENT LOT STATUS ----");
        for (VehicleType type : VehicleType.values()) {
            System.out.println(type + ": " + occupied.get(type) + "/" + capacity.get(type) + " occupied");
        }
        System.out.println("------------------------------");
    }
}
