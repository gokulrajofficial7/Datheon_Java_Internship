/**
 * Standard linear pricing strategy: Base Daily Rate * Days.
 */
public class StandardPricingStrategy implements PricingStrategy {
    @Override
    public double calculatePrice(double baseDailyRate, int rentalDays) {
        if (rentalDays <= 0) return 0.0;
        return baseDailyRate * rentalDays;
    }

    @Override
    public String getStrategyName() {
        return "Standard Flat Rate";
    }
}
