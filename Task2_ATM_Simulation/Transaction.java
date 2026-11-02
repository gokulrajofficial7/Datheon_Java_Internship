import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Immutable transaction record representing an audit log entry.
 */
public class Transaction {
    public enum Type {
        DEPOSIT,
        WITHDRAWAL,
        TRANSFER_OUT,
        TRANSFER_IN,
        INTEREST_CREDIT
    }

    private final LocalDateTime timestamp;
    private final Type type;
    private final double amount;
    private final double resultingBalance;
    private final String description;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Transaction(Type type, double amount, double resultingBalance, String description) {
        this.timestamp = LocalDateTime.now();
        this.type = type;
        this.amount = amount;
        this.resultingBalance = resultingBalance;
        this.description = description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Type getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public double getResultingBalance() {
        return resultingBalance;
    }

    public String getDescription() {
        return description;
    }

    public String toFormattedString() {
        return String.format("[%s] %-15s | Amount: $%9.2f | Balance: $%10.2f | Note: %s",
                timestamp.format(FORMATTER), type, amount, resultingBalance, description);
    }
}
