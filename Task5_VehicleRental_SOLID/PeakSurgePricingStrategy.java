/**
 * Surge pricing strategy adding a 20% surcharge during peak demand periods.
 */
public class PeakSurgePricingStrategy implements PricingStrategy {
    @Override
    public double calculatePrice(double baseDailyRate, int rentalDays) {
        if (rentalDays <= 0) return 0.0;
        return (baseDailyRate * rentalDays) * 1.20; // 20% surge
    }

    @Override
    public String getStrategyName() {
        return "Peak Weekend Surge (+20% Demand Markup)";
    }
}
