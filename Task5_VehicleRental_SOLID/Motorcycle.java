/**
 * Concrete Motorcycle representation.
 */
public class Motorcycle extends Vehicle {
    private final int engineCc;

    public Motorcycle(String vehicleId, String model, FuelType fuelType, double baseDailyRate, int engineCc) {
        super(vehicleId, model, VehicleType.MOTORCYCLE, fuelType, baseDailyRate);
        this.engineCc = engineCc;
    }

    public int getEngineCc() {
        return engineCc;
    }

    @Override
    public String getSpecifications() {
        return String.format("%d cc Sport/Commuter Engine", engineCc);
    }
}
