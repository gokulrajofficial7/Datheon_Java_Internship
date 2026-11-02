/**
 * Concrete Truck representation.
 */
public class Truck extends Vehicle {
    private final double payloadCapacityTons;

    public Truck(String vehicleId, String model, FuelType fuelType, double baseDailyRate, double payloadCapacityTons) {
        super(vehicleId, model, VehicleType.TRUCK, fuelType, baseDailyRate);
        this.payloadCapacityTons = payloadCapacityTons;
    }

    public double getPayloadCapacityTons() {
        return payloadCapacityTons;
    }

    @Override
    public String getSpecifications() {
        return String.format("%.1f Ton Heavy Freight Payload", payloadCapacityTons);
    }
}
