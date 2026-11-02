import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Digital Library Inventory & Checkout Management System.
 * Demonstrates Collections Framework (Map, List), java.time (LocalDate, ChronoUnit),
 * CSV catalog ingestion, and automated overdue penalty calculations.
 */
public class LibrarySystem {
    private final Map<String, Book> catalog; // Key: ISBN -> Book
    private final Map<String, Member> members; // Key: MemberID -> Member
    private final Map<String, LoanRecord> activeLoans; // Key: LoanID -> LoanRecord
    private final List<LoanRecord> loanAuditHistory;
    private final Scanner scanner;

    public static final double DAILY_LATE_FEE = 1.50; // $1.50 per overdue day

    public LibrarySystem() {
        this.catalog = new LinkedHashMap<>();
        this.members = new LinkedHashMap<>();
        this.activeLoans = new LinkedHashMap<>();
        this.loanAuditHistory = new ArrayList<>();
        this.scanner = new Scanner(System.in);

        seedDemoMembers();
        loadCatalogFromCSV("books_catalog.csv");
        seedSampleLoan();
    }

    public static void main(String[] args) {
        LibrarySystem app = new LibrarySystem();
        app.run();
    }

    private void seedDemoMembers() {
        Member m1 = new Member("MEM101", "Aarav Sharma", "aarav@example.com");
        Member m2 = new Member("MEM102", "Diya Patel", "diya@example.com");
        members.put(m1.getMemberId(), m1);
        members.put(m2.getMemberId(), m2);
    }

    private void seedSampleLoan() {
        // Pre-create a simulated past loan: checked out 20 days ago (due 6 days ago) to test overdue calculation
        Book book = catalog.get("978-0134685991"); // Effective Java
        if (book != null && book.borrowCopy()) {
            LocalDate checkoutDate = LocalDate.now().minusDays(20);
            LoanRecord loan = new LoanRecord("LOAN-001", "MEM101", book.getIsbn(), book.getTitle(), checkoutDate);
            activeLoans.put(loan.getLoanId(), loan);
            loanAuditHistory.add(loan);
            members.get("MEM101").addLoan(loan);
        }
    }

    public void loadCatalogFromCSV(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("[Warning] CSV catalog file not found at: " + filePath);
            return;
        }

        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String header = br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    String isbn = parts[0].trim();
                    String title = parts[1].trim();
                    String author = parts[2].trim();
                    String genre = parts[3].trim();
                    int year = Integer.parseInt(parts[4].trim());
                    int copies = Integer.parseInt(parts[5].trim());

                    Book book = new Book(isbn, title, author, genre, year, copies);
                    catalog.put(isbn, book);
                    count++;
                }
            }
            System.out.printf("[Catalog Ingested] Successfully loaded %d book titles from '%s'.%n", count, file.getName());
        } catch (IOException | NumberFormatException e) {
            System.out.println("[Error] Failed to load catalog CSV: " + e.getMessage());
        }
    }

    public void run() {
        System.out.println("=================================================================");
        System.out.println("     DATHEON - DIGITAL LIBRARY INVENTORY & CHECKOUT SUITE        ");
        System.out.println("=================================================================");

        boolean exit = false;
        while (!exit) {
            printMenu();
            System.out.print("Select an option (1-8): ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    displayCatalogTable(catalog.values());
                    break;
                case "2":
                    searchBooks();
                    break;
                case "3":
                    checkoutBook();
                    break;
                case "4":
                    returnBook();
                    break;
                case "5":
                    registerMember();
                    break;
                case "6":
                    viewActiveLoans();
                    break;
                case "7":
                    addNewBook();
                    break;
                case "8":
                    exit = true;
                    System.out.println("\nExiting Digital Library Management System. Happy Reading!");
                    break;
                default:
                    System.out.println("[Error] Invalid selection. Choose 1 to 8.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n--------------------- LIBRARY OPERATIONS ---------------------");
        System.out.println("1. Browse Entire Book Catalog");
        System.out.println("2. Search Catalog (Title, Author, Genre)");
        System.out.println("3. Borrow / Checkout a Book");
        System.out.println("4. Return a Book & Assess Overdue Penalties");
        System.out.println("5. Register New Library Member");
        System.out.println("6. View Active Loans & Overdue Status");
        System.out.println("7. Add New Book Title to Inventory");
        System.out.println("8. Exit");
        System.out.println("--------------------------------------------------------------");
    }

    private void displayCatalogTable(Collection<Book> books) {
        if (books.isEmpty()) {
            System.out.println("[Info] No books found matching the criteria.");
            return;
        }

        System.out.println("\n" + "=".repeat(105));
        System.out.println("                                DIGITAL LIBRARY INVENTORY CATALOG                                ");
        System.out.println("=".repeat(105));
        System.out.printf("| %-15s | %-24s | %-20s | %-16s | %-4s | %-7s |%n",
                "ISBN", "Title", "Author", "Genre", "Year", "Copies");
        System.out.println("-".repeat(105));

        for (Book b : books) {
            System.out.printf("| %-15s | %-24s | %-20s | %-16s | %-4d | %-7d |%n",
                    b.getIsbn(), truncate(b.getTitle(), 24), truncate(b.getAuthor(), 20),
                    truncate(b.getGenre(), 16), b.getYear(), b.getCopiesAvailable());
        }
        System.out.println("=".repeat(105));
    }

    private void searchBooks() {
        System.out.print("\nEnter keyword to search (Title substring, Author, or Genre): ");
        String term = scanner.nextLine().trim().toLowerCase();

        List<Book> results = new ArrayList<>();
        for (Book b : catalog.values()) {
            if (b.getTitle().toLowerCase().contains(term) ||
                b.getAuthor().toLowerCase().contains(term) ||
                b.getGenre().toLowerCase().contains(term)) {
                results.add(b);
            }
        }

        displayCatalogTable(results);
    }

    private void checkoutBook() {
        System.out.print("\nEnter Member ID (Demo: MEM101 or MEM102): ");
        String memberId = scanner.nextLine().trim();
        Member member = members.get(memberId);
        if (member == null) {
            System.out.println("[Error] Member ID not registered. Please register member first.");
            return;
        }

        System.out.print("Enter Book ISBN to Borrow: ");
        String isbn = scanner.nextLine().trim();
        Book book = catalog.get(isbn);
        if (book == null) {
            System.out.println("[Error] Book with ISBN '" + isbn + "' not found in catalog.");
            return;
        }

        if (!book.isAvailable()) {
            System.out.println("[Unavailable] All copies of '" + book.getTitle() + "' are currently checked out.");
            return;
        }

        // Perform borrow
        book.borrowCopy();
        String loanId = "LOAN-" + String.format("%03d", activeLoans.size() + loanAuditHistory.size() + 1);
        LoanRecord loan = new LoanRecord(loanId, member.getMemberId(), book.getIsbn(), book.getTitle(), LocalDate.now());

        activeLoans.put(loanId, loan);
        loanAuditHistory.add(loan);
        member.addLoan(loan);

        System.out.println("\n[Checkout Successful]");
        System.out.println("  Loan Reference ID : " + loan.getLoanId());
        System.out.println("  Book Borrowed     : " + book.getTitle());
        System.out.println("  Borrower          : " + member.getName() + " (" + member.getMemberId() + ")");
        System.out.println("  Checkout Date     : " + loan.getCheckoutDate());
        System.out.println("  Due Date          : " + loan.getDueDate() + " (Standard 14-day loan)");
        System.out.println("  Remaining Copies  : " + book.getCopiesAvailable());
    }

    private void returnBook() {
        System.out.print("\nEnter Loan ID (e.g., LOAN-001): ");
        String loanId = scanner.nextLine().trim();

        LoanRecord loan = activeLoans.get(loanId);
        if (loan == null) {
            System.out.println("[Error] Active loan record not found for ID: " + loanId);
            return;
        }

        System.out.print("Enter Return Date (YYYY-MM-DD) or press Enter for Today (" + LocalDate.now() + "): ");
        String dateInput = scanner.nextLine().trim();
        LocalDate returnDate = LocalDate.now();
        if (!dateInput.isEmpty()) {
            try {
                returnDate = LocalDate.parse(dateInput);
            } catch (DateTimeParseException e) {
                System.out.println("[Warning] Invalid date format. Defaulting to today.");
                returnDate = LocalDate.now();
            }
        }

        // Calculate and assess fine
        double fine = loan.completeReturn(returnDate, DAILY_LATE_FEE);
        long overdueDays = loan.getOverdueDays(returnDate);

        // Update book inventory
        Book book = catalog.get(loan.getIsbn());
        if (book != null) {
            book.returnCopy();
        }

        // Update member record
        Member member = members.get(loan.getMemberId());
        if (member != null) {
            member.removeLoan(loanId);
            if (fine > 0) member.recordFinePayment(fine);
        }

        activeLoans.remove(loanId);

        System.out.println("\n[Return Processed Successfully]");
        System.out.println("  Book Returned   : " + loan.getBookTitle());
        System.out.println("  Due Date        : " + loan.getDueDate());
        System.out.println("  Actual Return   : " + loan.getReturnDate());
        System.out.println("  Overdue Days    : " + overdueDays + " days");
        if (overdueDays > 0) {
            System.out.printf("  PENALTY FINE    : $%.2f (Rate: $%.2f / day)%n", fine, DAILY_LATE_FEE);
        } else {
            System.out.println("  Status          : Returned on time. No penalty incurred.");
        }
    }

    private void viewActiveLoans() {
        if (activeLoans.isEmpty()) {
            System.out.println("[Info] No active loans at this time.");
            return;
        }

        System.out.println("\n" + "=".repeat(95));
        System.out.println("                         ACTIVE LOANS & OVERDUE AUDIT TABLE                              ");
        System.out.println("=".repeat(95));
        System.out.printf("| %-9s | %-9s | %-22s | %-12s | %-12s | %-13s |%n",
                "Loan ID", "Member ID", "Book Title", "Checkout", "Due Date", "Status");
        System.out.println("-".repeat(95));

        LocalDate today = LocalDate.now();
        for (LoanRecord l : activeLoans.values()) {
            long overdue = l.getOverdueDays(today);
            String status = (overdue > 0)
                    ? String.format("OVERDUE (%dd)", overdue)
                    : "ACTIVE (On Time)";

            System.out.printf("| %-9s | %-9s | %-22s | %-12s | %-12s | %-13s |%n",
                    l.getLoanId(), l.getMemberId(), truncate(l.getBookTitle(), 22),
                    l.getCheckoutDate(), l.getDueDate(), status);
        }
        System.out.println("=".repeat(95));
    }

    private void registerMember() {
        System.out.print("\nEnter New Member ID (e.g. MEM103): ");
        String id = scanner.nextLine().trim();
        if (members.containsKey(id)) {
            System.out.println("[Error] Member ID already registered.");
            return;
        }

        System.out.print("Enter Full Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter Email Address: ");
        String email = scanner.nextLine().trim();

        try {
            Member m = new Member(id, name, email);
            members.put(id, m);
            System.out.printf("[Registered] Member '%s' (ID: %s) added successfully.%n", name, id);
        } catch (IllegalArgumentException e) {
            System.out.println("[Validation Error] " + e.getMessage());
        }
    }

    private void addNewBook() {
        try {
            System.out.print("\nEnter Book ISBN: ");
            String isbn = scanner.nextLine().trim();
            if (catalog.containsKey(isbn)) {
                System.out.println("[Error] A book with this ISBN already exists.");
                return;
            }

            System.out.print("Enter Title: ");
            String title = scanner.nextLine().trim();

            System.out.print("Enter Author: ");
            String author = scanner.nextLine().trim();

            System.out.print("Enter Genre: ");
            String genre = scanner.nextLine().trim();

            System.out.print("Enter Publication Year: ");
            int year = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter Initial Available Copies: ");
            int copies = Integer.parseInt(scanner.nextLine().trim());

            Book book = new Book(isbn, title, author, genre, year, copies);
            catalog.put(isbn, book);
            System.out.printf("[Success] '%s' added to library catalog.%n", title);
        } catch (NumberFormatException e) {
            System.out.println("[Error] Year and copies must be valid numbers.");
        } catch (IllegalArgumentException e) {
            System.out.println("[Validation Error] " + e.getMessage());
        }
    }

    private String truncate(String text, int max) {
        if (text == null) return "";
        return text.length() <= max ? text : text.substring(0, max - 3) + "...";
    }
}
