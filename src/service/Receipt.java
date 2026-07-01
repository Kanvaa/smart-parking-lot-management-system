package service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Receipt {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private final String vehicleNumber;
    private final String vehicleType;
    private final LocalDateTime entryTime;
    private final LocalDateTime exitTime;
    private final double amount;

    public Receipt(String vehicleNumber, String vehicleType, LocalDateTime entryTime,
                    LocalDateTime exitTime, double amount) {
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public void print() {
        System.out.println("------ PARKING RECEIPT ------");
        System.out.println("Vehicle Number : " + vehicleNumber);
        System.out.println("Vehicle Type   : " + vehicleType);
        System.out.println("Entry Time     : " + entryTime.format(FMT));
        System.out.println("Exit Time      : " + exitTime.format(FMT));
        System.out.printf("Amount Due     : Rs. %.2f%n", amount);
        System.out.println("------------------------------");
    }
}
