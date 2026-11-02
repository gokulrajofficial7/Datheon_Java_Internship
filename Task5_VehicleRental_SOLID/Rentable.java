/**
 * Segregated interface focusing strictly on vehicle rental contracts.
 * Demonstrates the Interface Segregation Principle (ISP).
 */
public interface Rentable {
    String getVehicleId();
    String getModel();
    VehicleType getVehicleType();
    FuelType getFuelType();
    double getBaseDailyRate();
    boolean isRented();
    void rentVehicle();
    void returnVehicle();
}
