/**
 * Long-term rental discount strategy applying:
 * - 15% discount for rentals > 7 days
 * - 25% discount for rentals > 30 days
 */
public class LongTermDiscountStrategy implements PricingStrategy {
    @Override
    public double calculatePrice(double baseDailyRate, int rentalDays) {
        if (rentalDays <= 0) return 0.0;
        double rawPrice = baseDailyRate * rentalDays;

        if (rentalDays >= 30) {
            return rawPrice * 0.75; // 25% discount
        } else if (rentalDays >= 7) {
            return rawPrice * 0.85; // 15% discount
        }
        return rawPrice;
    }

    @Override
    public String getStrategyName() {
        return "Long-Term Discount (15% for 7d+, 25% for 30d+)";
    }
}
