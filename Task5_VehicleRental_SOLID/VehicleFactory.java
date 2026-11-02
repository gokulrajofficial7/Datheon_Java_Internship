/**
 * Creational Factory Pattern implementation.
 * Decouples vehicle instantiation logic from client business logic.
 */
public class VehicleFactory {

    /**
     * Factory creation method instantiating the appropriate concrete vehicle.
     */
    public static Vehicle createVehicle(VehicleType type, String vehicleId, String model,
                                         FuelType fuelType, double baseDailyRate, Object specificParam) {
        if (type == null) {
            throw new IllegalArgumentException("Vehicle type cannot be null.");
        }

        switch (type) {
            case CAR:
                int seats = (specificParam instanceof Number) ? ((Number) specificParam).intValue() : 5;
                return new Car(vehicleId, model, fuelType, baseDailyRate, seats);

            case TRUCK:
                double payload = (specificParam instanceof Number) ? ((Number) specificParam).doubleValue() : 2.5;
                return new Truck(vehicleId, model, fuelType, baseDailyRate, payload);

            case MOTORCYCLE:
                int cc = (specificParam instanceof Number) ? ((Number) specificParam).intValue() : 250;
                return new Motorcycle(vehicleId, model, fuelType, baseDailyRate, cc);

            default:
                throw new IllegalArgumentException("Unsupported vehicle classification: " + type);
        }
    }
}
