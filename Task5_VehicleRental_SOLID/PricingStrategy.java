/**
 * Strategy interface abstracting dynamic rate calculation from vehicle classes.
 * Demonstrates the Single Responsibility Principle (SRP) and Open/Closed Principle (OCP).
 */
public interface PricingStrategy {
    double calculatePrice(double baseDailyRate, int rentalDays);
    String getStrategyName();
}
