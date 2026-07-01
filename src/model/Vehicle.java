package model;

import java.time.LocalDateTime;

public abstract class Vehicle {
    private final String vehicleNumber;
    private final VehicleType type;
    private LocalDateTime entryTime;

    public Vehicle(String vehicleNumber, VehicleType type) {
        this.vehicleNumber = vehicleNumber;
        this.type = type;
        this.entryTime = LocalDateTime.now();
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public VehicleType getType() {
        return type;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    // Each subclass defines its own hourly rate -> demonstrates polymorphism
    public abstract double getHourlyRate();

    @Override
    public String toString() {
        return type + " [" + vehicleNumber + "]";
    }
}
