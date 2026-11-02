import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Abstract base class representing a generic bank account.
 * Demonstrates OOP Abstraction, Encapsulation, and Polymorphism.
 */
public abstract class Account {
    protected final String accountNumber;
    protected double balance;
    protected final List<Transaction> transactionHistory;

    public Account(String accountNumber, double initialBalance) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Account number cannot be empty.");
        }
        if (initialBalance < 0.0) {
            throw new IllegalArgumentException("Initial balance cannot be negative.");
        }
        this.accountNumber = accountNumber.trim();
        this.balance = initialBalance;
        this.transactionHistory = new ArrayList<>();

        if (initialBalance > 0.0) {
            transactionHistory.add(new Transaction(
                    Transaction.Type.DEPOSIT,
                    initialBalance,
                    this.balance,
                    "Initial Opening Deposit"
            ));
        }
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public List<Transaction> getTransactionHistory() {
        return Collections.unmodifiableList(transactionHistory);
    }

    /**
     * Common deposit logic shared across all account types.
     */
    public synchronized void deposit(double amount) {
        if (amount <= 0.0) {
            throw new IllegalArgumentException("Deposit amount must be strictly positive.");
        }
        this.balance += amount;
        transactionHistory.add(new Transaction(
                Transaction.Type.DEPOSIT,
                amount,
                this.balance,
                "Cash / Counter Deposit"
        ));
    }

    /**
     * Abstract withdrawal method: Subclasses enforce specific business rules
     * (e.g. minimum balance for Savings, overdraft limits for Checking).
     */
    public abstract void withdraw(double amount) throws InsufficientFundsException;

    /**
     * Inter-account fund transfer demonstrating transactional integrity.
     */
    public synchronized void transferTo(Account targetAccount, double amount) throws InsufficientFundsException {
        if (targetAccount == null) {
            throw new IllegalArgumentException("Target account cannot be null.");
        }
        if (targetAccount.getAccountNumber().equals(this.accountNumber)) {
            throw new IllegalArgumentException("Cannot transfer funds to the same account.");
        }
        if (amount <= 0.0) {
            throw new IllegalArgumentException("Transfer amount must be strictly positive.");
        }

        // Subclass withdraw will validate and deduct balance
        this.withdraw(amount);
        
        // Remove generic withdrawal log and replace with descriptive transfer log
        transactionHistory.remove(transactionHistory.size() - 1);
        transactionHistory.add(new Transaction(
                Transaction.Type.TRANSFER_OUT,
                amount,
                this.balance,
                "Transfer to Account #" + targetAccount.getAccountNumber()
        ));

        // Credit target account
        targetAccount.balance += amount;
        targetAccount.transactionHistory.add(new Transaction(
                Transaction.Type.TRANSFER_IN,
                amount,
                targetAccount.getBalance(),
                "Transfer received from Account #" + this.accountNumber
        ));
    }

    public void printMiniStatement() {
        System.out.println("\n-----------------------------------------------------------------------------------------");
        System.out.printf(" MINI STATEMENT: Account #%s (%s)%n", accountNumber, getAccountType());
        System.out.printf(" Current Balance: $%.2f%n", balance);
        System.out.println("-----------------------------------------------------------------------------------------");
        if (transactionHistory.isEmpty()) {
            System.out.println("No transaction activity recorded.");
        } else {
            // Print last 10 transactions
            int start = Math.max(0, transactionHistory.size() - 10);
            for (int i = start; i < transactionHistory.size(); i++) {
                System.out.println(transactionHistory.get(i).toFormattedString());
            }
        }
        System.out.println("-----------------------------------------------------------------------------------------");
    }

    public abstract String getAccountType();

    public abstract String getAccountSummary();
}
