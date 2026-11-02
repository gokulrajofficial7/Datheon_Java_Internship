import java.time.LocalDate;

/**
 * Abstract base vehicle class adhering to the Liskov Substitution Principle (LSP).
 * Encapsulates core identity, state, rental mechanics, and odometer telemetry.
 */
public abstract class Vehicle implements Rentable, Maintainable {
    protected final String vehicleId;
    protected final String model;
    protected final VehicleType vehicleType;
    protected final FuelType fuelType;
    protected final double baseDailyRate;
    protected boolean isRented;
    protected int odometerMileage;
    protected LocalDate lastServiceDate;

    public static final int SERVICE_INTERVAL_MILES = 5000;

    public Vehicle(String vehicleId, String model, VehicleType vehicleType, FuelType fuelType, double baseDailyRate) {
        if (vehicleId == null || vehicleId.trim().isEmpty()) {
            throw new IllegalArgumentException("Vehicle ID cannot be empty.");
        }
        if (model == null || model.trim().isEmpty()) {
            throw new IllegalArgumentException("Vehicle model cannot be empty.");
        }
        if (baseDailyRate <= 0.0) {
            throw new IllegalArgumentException("Base daily rate must be positive.");
        }
        this.vehicleId = vehicleId.trim();
        this.model = model.trim();
        this.vehicleType = vehicleType;
        this.fuelType = fuelType;
        this.baseDailyRate = baseDailyRate;
        this.isRented = false;
        this.odometerMileage = 1000; // baseline odometer
        this.lastServiceDate = LocalDate.now().minusMonths(2);
    }

    @Override
    public String getVehicleId() {
        return vehicleId;
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public VehicleType getVehicleType() {
        return vehicleType;
    }

    @Override
    public FuelType getFuelType() {
        return fuelType;
    }

    @Override
    public double getBaseDailyRate() {
        return baseDailyRate;
    }

    @Override
    public boolean isRented() {
        return isRented;
    }

    @Override
    public synchronized void rentVehicle() {
        if (isRented) {
            throw new IllegalStateException("Vehicle '" + vehicleId + "' is already currently rented out.");
        }
        this.isRented = true;
    }

    @Override
    public synchronized void returnVehicle() {
        this.isRented = false;
    }

    @Override
    public int getOdometerMileage() {
        return odometerMileage;
    }

    @Override
    public LocalDate getLastServiceDate() {
        return lastServiceDate;
    }

    @Override
    public void updateMileage(int milesDriven) {
        if (milesDriven > 0) {
            this.odometerMileage += milesDriven;
        }
    }

    @Override
    public void performMaintenance() {
        this.lastServiceDate = LocalDate.now();
    }

    @Override
    public boolean requiresService() {
        return (odometerMileage % SERVICE_INTERVAL_MILES) >= 4000;
    }

    public abstract String getSpecifications();
}
