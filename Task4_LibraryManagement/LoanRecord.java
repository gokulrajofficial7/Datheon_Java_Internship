import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Tracks loan checkout state, return date, and automated overdue penalty calculations.
 * Demonstrates modern Java Date/Time API (java.time.LocalDate, ChronoUnit).
 */
public class LoanRecord {
    private final String loanId;
    private final String memberId;
    private final String isbn;
    private final String bookTitle;
    private final LocalDate checkoutDate;
    private final LocalDate dueDate;
    private LocalDate returnDate;
    private boolean isReturned;
    private double fineAmount;

    public static final int STANDARD_LOAN_PERIOD_DAYS = 14;

    public LoanRecord(String loanId, String memberId, String isbn, String bookTitle, LocalDate checkoutDate) {
        this.loanId = loanId;
        this.memberId = memberId;
        this.isbn = isbn;
        this.bookTitle = bookTitle;
        this.checkoutDate = (checkoutDate != null) ? checkoutDate : LocalDate.now();
        this.dueDate = this.checkoutDate.plusDays(STANDARD_LOAN_PERIOD_DAYS);
        this.returnDate = null;
        this.isReturned = false;
        this.fineAmount = 0.0;
    }

    public String getLoanId() {
        return loanId;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public boolean isReturned() {
        return isReturned;
    }

    public double getFineAmount() {
        return fineAmount;
    }

    public long getOverdueDays(LocalDate referenceDate) {
        if (referenceDate == null) referenceDate = LocalDate.now();
        if (referenceDate.isAfter(dueDate)) {
            return ChronoUnit.DAYS.between(dueDate, referenceDate);
        }
        return 0;
    }

    /**
     * Completes book return and assesses penalty fines if overdue.
     */
    public double completeReturn(LocalDate actualReturnDate, double dailyFineRate) {
        this.returnDate = (actualReturnDate != null) ? actualReturnDate : LocalDate.now();
        this.isReturned = true;

        long overdueDays = getOverdueDays(this.returnDate);
        if (overdueDays > 0) {
            this.fineAmount = overdueDays * dailyFineRate;
        } else {
            this.fineAmount = 0.0;
        }
        return this.fineAmount;
    }
}
