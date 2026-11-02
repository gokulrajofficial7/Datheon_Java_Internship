/**
 * Thrown when an invalid PIN is entered during customer authentication.
 */
public class InvalidPINException extends Exception {
    private final int remainingAttempts;

    public InvalidPINException(String message, int remainingAttempts) {
        super(message);
        this.remainingAttempts = remainingAttempts;
    }

    public int getRemainingAttempts() {
        return remainingAttempts;
    }
}
