/**
 * Concrete Car representation.
 */
public class Car extends Vehicle {
    private final int seatingCapacity;

    public Car(String vehicleId, String model, FuelType fuelType, double baseDailyRate, int seatingCapacity) {
        super(vehicleId, model, VehicleType.CAR, fuelType, baseDailyRate);
        this.seatingCapacity = seatingCapacity;
    }

    public int getSeatingCapacity() {
        return seatingCapacity;
    }

    @Override
    public String getSpecifications() {
        return String.format("%d-Seater Passenger Sedan", seatingCapacity);
    }
}
