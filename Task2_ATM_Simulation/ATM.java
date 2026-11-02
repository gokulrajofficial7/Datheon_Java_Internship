import java.util.*;

/**
 * Interactive Banking & ATM Simulation Engine.
 * Demonstrates OOP Inheritance, Polymorphism, Custom Exceptions, and Transaction Logging.
 */
public class ATM {
    private final Map<String, Customer> customerDatabase;
    private final Map<String, Account> globalAccountLookup;
    private final Scanner scanner;

    public ATM() {
        this.customerDatabase = new HashMap<>();
        this.globalAccountLookup = new HashMap<>();
        this.scanner = new Scanner(System.in);
        seedDemoData();
    }

    public static void main(String[] args) {
        ATM atm = new ATM();
        atm.startSession();
    }

    private void seedDemoData() {
        // Customer 1: Aarav Sharma
        Customer c1 = new Customer("1001", "Aarav Sharma", "1234");
        SavingsAccount sa1 = new SavingsAccount("SA-1001", 2500.00, 4.50, 100.00);
        CheckingAccount ca1 = new CheckingAccount("CA-2001", 800.00, 500.00);
        c1.addAccount(sa1);
        c1.addAccount(ca1);
        registerCustomer(c1);

        // Customer 2: Diya Patel
        Customer c2 = new Customer("1002", "Diya Patel", "4321");
        SavingsAccount sa2 = new SavingsAccount("SA-1002", 5000.00, 5.00, 200.00);
        CheckingAccount ca2 = new CheckingAccount("CA-2002", 1500.00, 1000.00);
        c2.addAccount(sa2);
        c2.addAccount(ca2);
        registerCustomer(c2);
    }

    private void registerCustomer(Customer customer) {
        customerDatabase.put(customer.getCustomerId(), customer);
        for (Account acc : customer.getAccounts().values()) {
            globalAccountLookup.put(acc.getAccountNumber(), acc);
        }
    }

    public void startSession() {
        System.out.println("=================================================================");
        System.out.println("        DATHEON - SECURE CORE BANKING & ATM SIMULATOR            ");
        System.out.println("=================================================================");

        boolean keepRunning = true;
        while (keepRunning) {
            System.out.println("\n[ATM WELCOME SCREEN]");
            System.out.println("1. Insert ATM Card / Login with Customer ID");
            System.out.println("2. Open New Customer Bank Profile");
            System.out.println("3. Power Off ATM");
            System.out.print("Select an option (1-3): ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    handleLogin();
                    break;
                case "2":
                    handleNewCustomerRegistration();
                    break;
                case "3":
                    keepRunning = false;
                    System.out.println("\nATM Terminal shutting down securely. Thank you!");
                    break;
                default:
                    System.out.println("[Error] Invalid choice. Please choose 1, 2, or 3.");
            }
        }
    }

    private void handleLogin() {
        System.out.print("\nEnter Customer ID (Demo: 1001 or 1002): ");
        String custId = scanner.nextLine().trim();

        Customer customer = customerDatabase.get(custId);
        if (customer == null) {
            System.out.println("[Security Alert] Customer record not found.");
            return;
        }

        if (customer.isLocked()) {
            System.out.println("[Access Denied] This account is LOCKED due to prior security violations.");
            return;
        }

        System.out.print("Enter 4-Digit Security PIN (Demo for 1001 is 1234): ");
        String enteredPin = scanner.nextLine().trim();

        try {
            customer.authenticatePIN(enteredPin);
            System.out.println("[Authentication Success] Welcome, " + customer.getName() + "!");
            runCustomerPortal(customer);
        } catch (InvalidPINException e) {
            System.out.println("[Authentication Error] " + e.getMessage());
        }
    }

    private void runCustomerPortal(Customer customer) {
        if (customer.getAccounts().isEmpty()) {
            System.out.println("[Warning] No active accounts associated with this customer profile.");
            return;
        }

        // Select an active account
        Account currentAccount = selectAccount(customer);
        if (currentAccount == null) return;

        boolean sessionActive = true;
        while (sessionActive) {
            System.out.println("\n-------------------------------------------------------------");
            System.out.printf(" ACTIVE ACCOUNT: %s (%s) | Balance: $%.2f%n",
                    currentAccount.getAccountNumber(), currentAccount.getAccountType(), currentAccount.getBalance());
            System.out.println("-------------------------------------------------------------");
            System.out.println("1. Check Balance & Account Details");
            System.out.println("2. Cash Deposit");
            System.out.println("3. Cash Withdrawal (Demonstrates Custom Exception Handling)");
            System.out.println("4. Fund Transfer to Another Account");
            System.out.println("5. Apply Monthly Interest (Savings Account Only)");
            System.out.println("6. Print Mini-Statement (Audit Log)");
            System.out.println("7. Switch Selected Account");
            System.out.println("8. Logout / End Session");
            System.out.print("Choose transaction (1-8): ");
            String opt = scanner.nextLine().trim();

            switch (opt) {
                case "1":
                    System.out.println("\n" + currentAccount.getAccountSummary());
                    break;
                case "2":
                    performDeposit(currentAccount);
                    break;
                case "3":
                    performWithdrawal(currentAccount);
                    break;
                case "4":
                    performTransfer(currentAccount);
                    break;
                case "5":
                    performInterestCredit(currentAccount);
                    break;
                case "6":
                    currentAccount.printMiniStatement();
                    break;
                case "7":
                    Account switched = selectAccount(customer);
                    if (switched != null) currentAccount = switched;
                    break;
                case "8":
                    sessionActive = false;
                    System.out.println("[Session Closed] Thank you for banking with Datheon.");
                    break;
                default:
                    System.out.println("[Error] Invalid selection. Choose 1-8.");
            }
        }
    }

    private Account selectAccount(Customer customer) {
        List<Account> accList = new ArrayList<>(customer.getAccounts().values());
        if (accList.size() == 1) {
            return accList.get(0);
        }

        System.out.println("\nSelect Account to Transact:");
        for (int i = 0; i < accList.size(); i++) {
            Account acc = accList.get(i);
            System.out.printf("  %d. %s [%s] - Balance: $%.2f%n", (i + 1), acc.getAccountNumber(), acc.getAccountType(), acc.getBalance());
        }
        System.out.print("Enter choice (1-" + accList.size() + "): ");
        try {
            int sel = Integer.parseInt(scanner.nextLine().trim());
            if (sel >= 1 && sel <= accList.size()) {
                return accList.get(sel - 1);
            }
        } catch (NumberFormatException ignored) {}
        System.out.println("[Warning] Invalid selection, defaulting to primary account.");
        return accList.get(0);
    }

    private void performDeposit(Account account) {
        try {
            System.out.print("Enter deposit amount ($): ");
            double amt = Double.parseDouble(scanner.nextLine().trim());
            account.deposit(amt);
            System.out.printf("[Deposit Complete] Credited $%.2f. New Balance: $%.2f%n", amt, account.getBalance());
        } catch (NumberFormatException e) {
            System.out.println("[Error] Invalid numeric amount entered.");
        } catch (IllegalArgumentException e) {
            System.out.println("[Transaction Rejected] " + e.getMessage());
        }
    }

    private void performWithdrawal(Account account) {
        try {
            System.out.print("Enter withdrawal amount ($): ");
            double amt = Double.parseDouble(scanner.nextLine().trim());

            // Polymorphic call: Subclass logic decides approval/rejection
            account.withdraw(amt);
            System.out.printf("[Dispensing Cash] Withdrew $%.2f successfully. Remaining Balance: $%.2f%n", amt, account.getBalance());
        } catch (NumberFormatException e) {
            System.out.println("[Error] Invalid numeric amount entered.");
        } catch (InsufficientFundsException e) {
            // Demonstrates handling of custom domain exception
            System.out.println("\n[TRANSACTION DECLINED - INSUFFICIENT FUNDS]");
            System.out.println("Reason           : " + e.getMessage());
            System.out.printf("Requested Amount : $%.2f%n", e.getRequestedAmount());
            System.out.printf("Available Balance: $%.2f%n", e.getAvailableBalance());
        } catch (IllegalArgumentException e) {
            System.out.println("[Transaction Rejected] " + e.getMessage());
        }
    }

    private void performTransfer(Account sourceAccount) {
        try {
            System.out.print("Enter destination Account Number (e.g., SA-1002, CA-2002): ");
            String destAccNo = scanner.nextLine().trim();

            Account destAccount = globalAccountLookup.get(destAccNo);
            if (destAccount == null) {
                System.out.println("[Error] Target account #" + destAccNo + " does not exist in banking network.");
                return;
            }

            System.out.print("Enter transfer amount ($): ");
            double amt = Double.parseDouble(scanner.nextLine().trim());

            sourceAccount.transferTo(destAccount, amt);
            System.out.printf("[Transfer Success] $%.2f transferred to %s. Source Balance: $%.2f%n",
                    amt, destAccNo, sourceAccount.getBalance());
        } catch (NumberFormatException e) {
            System.out.println("[Error] Invalid numeric amount entered.");
        } catch (InsufficientFundsException e) {
            System.out.println("[Transfer Failed] " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("[Validation Error] " + e.getMessage());
        }
    }

    private void performInterestCredit(Account account) {
        if (account instanceof SavingsAccount) {
            SavingsAccount sa = (SavingsAccount) account;
            double oldBal = sa.getBalance();
            sa.applyInterest();
            System.out.printf("[Interest Credited] Previous Balance: $%.2f | New Balance: $%.2f (Added $%.2f)%n",
                    oldBal, sa.getBalance(), (sa.getBalance() - oldBal));
        } else {
            System.out.println("[Notice] Interest credit applies only to Savings Accounts.");
        }
    }

    private void handleNewCustomerRegistration() {
        try {
            System.out.print("Enter New Customer ID (e.g., 1003): ");
            String id = scanner.nextLine().trim();
            if (customerDatabase.containsKey(id)) {
                System.out.println("[Error] Customer ID already exists.");
                return;
            }

            System.out.print("Enter Customer Full Name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Set 4-Digit Security PIN: ");
            String pin = scanner.nextLine().trim();

            Customer customer = new Customer(id, name, pin);

            // Open default Savings Account
            String saNum = "SA-" + id;
            SavingsAccount sa = new SavingsAccount(saNum, 100.00, 4.00, 50.00);
            customer.addAccount(sa);

            // Open default Checking Account
            String caNum = "CA-" + id;
            CheckingAccount ca = new CheckingAccount(caNum, 50.00, 200.00);
            customer.addAccount(ca);

            registerCustomer(customer);
            System.out.printf("[Success] Customer '%s' registered with Savings Account #%s and Checking Account #%s!%n",
                    name, saNum, caNum);
        } catch (IllegalArgumentException e) {
            System.out.println("[Validation Error] " + e.getMessage());
        }
    }
}
