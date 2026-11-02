/**
 * Models a transactional Checking Account with an overdraft protection limit.
 * Demonstrates OOP Inheritance and Method Overriding.
 */
public class CheckingAccount extends Account {
    private final double overdraftLimit; // Allowable negative balance limit

    public CheckingAccount(String accountNumber, double initialBalance, double overdraftLimit) {
        super(accountNumber, initialBalance);
        if (overdraftLimit < 0.0) {
            throw new IllegalArgumentException("Overdraft limit cannot be negative.");
        }
        this.overdraftLimit = overdraftLimit;
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    @Override
    public synchronized void withdraw(double amount) throws InsufficientFundsException {
        if (amount <= 0.0) {
            throw new IllegalArgumentException("Withdrawal amount must be strictly positive.");
        }
        double available = balance + overdraftLimit;
        if (amount > available) {
            throw new InsufficientFundsException(
                    String.format("Checking Account overdraft exceeded. Total available capacity (Balance + Overdraft): $%.2f",
                            available),
                    amount,
                    balance
            );
        }

        this.balance -= amount;
        String note = (balance < 0.0)
                ? String.format("ATM Cash Withdrawal (Overdraft in use: $%.2f)", Math.abs(balance))
                : "ATM Cash Withdrawal (Checking)";

        transactionHistory.add(new Transaction(
                Transaction.Type.WITHDRAWAL,
                amount,
                this.balance,
                note
        ));
    }

    @Override
    public String getAccountType() {
        return "Checking Account";
    }

    @Override
    public String getAccountSummary() {
        return String.format("Checking Account [No: %s | Balance: $%.2f | Overdraft Limit: $%.2f | Effective Buying Power: $%.2f]",
                accountNumber, balance, overdraftLimit, (balance + overdraftLimit));
    }
}
