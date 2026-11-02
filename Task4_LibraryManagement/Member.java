import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Models a library member profile with loan relationships.
 */
public class Member {
    private final String memberId;
    private final String name;
    private final String email;
    private final List<LoanRecord> activeLoans;
    private double totalFinesPaid;

    public Member(String memberId, String name, String email) {
        if (memberId == null || memberId.trim().isEmpty()) {
            throw new IllegalArgumentException("Member ID cannot be empty.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Member name cannot be empty.");
        }
        this.memberId = memberId.trim();
        this.name = name.trim();
        this.email = (email != null) ? email.trim() : "";
        this.activeLoans = new ArrayList<>();
        this.totalFinesPaid = 0.0;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public List<LoanRecord> getActiveLoans() {
        return Collections.unmodifiableList(activeLoans);
    }

    public void addLoan(LoanRecord loan) {
        activeLoans.add(loan);
    }

    public boolean removeLoan(String loanId) {
        return activeLoans.removeIf(l -> l.getLoanId().equals(loanId));
    }

    public void recordFinePayment(double fine) {
        this.totalFinesPaid += fine;
    }

    public double getTotalFinesPaid() {
        return totalFinesPaid;
    }
}
