/**
 * Thrown when an account has insufficient funds (or exceeds overdraft limit) to complete a transaction.
 */
public class InsufficientFundsException extends Exception {
    private final double requestedAmount;
    private final double availableBalance;

    public InsufficientFundsException(String message, double requestedAmount, double availableBalance) {
        super(message);
        this.requestedAmount = requestedAmount;
        this.availableBalance = availableBalance;
    }

    public double getRequestedAmount() {
        return requestedAmount;
    }

    public double getAvailableBalance() {
        return availableBalance;
    }
}
