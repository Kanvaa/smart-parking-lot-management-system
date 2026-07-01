package model;

public class Truck extends Vehicle {
    private static final double HOURLY_RATE = 35.0;

    public Truck(String vehicleNumber) {
        super(vehicleNumber, VehicleType.TRUCK);
    }

    @Override
    public double getHourlyRate() {
        return HOURLY_RATE;
    }
}
