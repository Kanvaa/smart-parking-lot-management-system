package model;

public class Car extends Vehicle {
    private static final double HOURLY_RATE = 20.0;

    public Car(String vehicleNumber) {
        super(vehicleNumber, VehicleType.CAR);
    }

    @Override
    public double getHourlyRate() {
        return HOURLY_RATE;
    }
}
