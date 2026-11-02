import java.time.LocalDate;

/**
 * Segregated interface focusing strictly on vehicle maintenance and servicing schedules.
 * Demonstrates the Interface Segregation Principle (ISP).
 */
public interface Maintainable {
    int getOdometerMileage();
    LocalDate getLastServiceDate();
    void updateMileage(int milesDriven);
    void performMaintenance();
    boolean requiresService();
}
