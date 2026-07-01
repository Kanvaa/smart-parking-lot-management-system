package model;

public class Bike extends Vehicle {
    private static final double HOURLY_RATE = 10.0;

    public Bike(String vehicleNumber) {
        super(vehicleNumber, VehicleType.BIKE);
    }

    @Override
    public double getHourlyRate() {
        return HOURLY_RATE;
    }
}
