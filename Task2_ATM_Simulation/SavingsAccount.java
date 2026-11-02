/**
 * Models an interest-bearing Savings Account with minimum balance enforcement.
 * Demonstrates OOP Inheritance and Method Overriding.
 */
public class SavingsAccount extends Account {
    private final double interestRate; // Annual interest percentage (e.g. 4.5%)
    private final double minimumBalance; // Minimum balance required to avoid penalty/decline

    public SavingsAccount(String accountNumber, double initialBalance, double interestRate, double minimumBalance) {
        super(accountNumber, initialBalance);
        if (interestRate < 0.0) {
            throw new IllegalArgumentException("Interest rate cannot be negative.");
        }
        if (minimumBalance < 0.0) {
            throw new IllegalArgumentException("Minimum balance cannot be negative.");
        }
        this.interestRate = interestRate;
        this.minimumBalance = minimumBalance;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public double getMinimumBalance() {
        return minimumBalance;
    }

    @Override
    public synchronized void withdraw(double amount) throws InsufficientFundsException {
        if (amount <= 0.0) {
            throw new IllegalArgumentException("Withdrawal amount must be strictly positive.");
        }
        double available = balance - minimumBalance;
        if (amount > available) {
            throw new InsufficientFundsException(
                    String.format("Savings Account withdrawal declined. Available withdrawable funds: $%.2f (Preserving $%.2f minimum balance).",
                            Math.max(0.0, available), minimumBalance),
                    amount,
                    balance
            );
        }

        this.balance -= amount;
        transactionHistory.add(new Transaction(
                Transaction.Type.WITHDRAWAL,
                amount,
                this.balance,
                "ATM Cash Withdrawal (Savings)"
        ));
    }

    /**
     * Calculates and credits interest based on the current balance.
     */
    public synchronized void applyInterest() {
        if (balance <= 0.0) return;
        double monthlyRate = (interestRate / 100.0) / 12.0;
        double interestAmount = balance * monthlyRate;

        this.balance += interestAmount;
        transactionHistory.add(new Transaction(
                Transaction.Type.INTEREST_CREDIT,
                interestAmount,
                this.balance,
                String.format("Monthly Interest Credited (%.2f%% p.a.)", interestRate)
        ));
    }

    @Override
    public String getAccountType() {
        return "Savings Account";
    }

    @Override
    public String getAccountSummary() {
        return String.format("Savings Account [No: %s | Balance: $%.2f | Int. Rate: %.2f%% | Min. Balance: $%.2f]",
                accountNumber, balance, interestRate, minimumBalance);
    }
}
