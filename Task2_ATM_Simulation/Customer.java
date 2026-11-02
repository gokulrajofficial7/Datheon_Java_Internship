import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Models a bank customer with credential security and account ownership.
 */
public class Customer {
    private final String customerId;
    private final String name;
    private final String pin;
    private int failedAttempts;
    private boolean isLocked;
    private final Map<String, Account> accounts;

    private static final int MAX_FAILED_ATTEMPTS = 3;

    public Customer(String customerId, String name, String pin) {
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer ID cannot be empty.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be empty.");
        }
        if (pin == null || pin.trim().length() < 4) {
            throw new IllegalArgumentException("PIN must be at least 4 digits.");
        }
        this.customerId = customerId.trim();
        this.name = name.trim();
        this.pin = pin.trim();
        this.failedAttempts = 0;
        this.isLocked = false;
        this.accounts = new LinkedHashMap<>();
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void addAccount(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null.");
        }
        accounts.put(account.getAccountNumber(), account);
    }

    public Account getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public Map<String, Account> getAccounts() {
        return Collections.unmodifiableMap(accounts);
    }

    /**
     * Authenticates customer PIN. Throws InvalidPINException on mismatch
     * and locks account after MAX_FAILED_ATTEMPTS.
     */
    public void authenticatePIN(String inputPin) throws InvalidPINException {
        if (isLocked) {
            throw new InvalidPINException("Account is LOCKED due to excessive failed attempts. Contact branch support.", 0);
        }

        if (this.pin.equals(inputPin)) {
            this.failedAttempts = 0; // Reset upon successful auth
        } else {
            this.failedAttempts++;
            int remaining = MAX_FAILED_ATTEMPTS - this.failedAttempts;
            if (remaining <= 0) {
                this.isLocked = true;
                throw new InvalidPINException("Incorrect PIN entered. Account has been LOCKED for security.", 0);
            } else {
                throw new InvalidPINException("Incorrect PIN entered. Attempts remaining: " + remaining, remaining);
            }
        }
    }
}
