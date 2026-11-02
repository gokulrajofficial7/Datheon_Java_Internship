import java.util.*;
import java.util.stream.Collectors;

/**
 * Enterprise Vehicle Dispatch and Rental Platform.
 * Demonstrates:
 *  - SOLID Principles (Single Responsibility, Open/Closed, Liskov Substitution, Interface Segregation, Dependency Inversion)
 *  - Creational Factory Pattern (VehicleFactory)
 *  - Behavioral Strategy Pattern (PricingStrategy)
 *  - Java 8+ Streams (filtering, mapping, sorting)
 */
public class VehicleRentalApp {
    private final List<Vehicle> fleet;
    private final Map<String, PricingStrategy> pricingStrategies;
    private final Scanner scanner;

    public VehicleRentalApp() {
        this.fleet = new ArrayList<>();
        this.pricingStrategies = new LinkedHashMap<>();
        this.scanner = new Scanner(System.in);

        registerStrategies();
        seedFleet();
    }

    public static void main(String[] args) {
        VehicleRentalApp app = new VehicleRentalApp();
        app.run();
    }

    private void registerStrategies() {
        pricingStrategies.put("1", new StandardPricingStrategy());
        pricingStrategies.put("2", new LongTermDiscountStrategy());
        pricingStrategies.put("3", new PeakSurgePricingStrategy());
    }

    private void seedFleet() {
        fleet.add(VehicleFactory.createVehicle(VehicleType.CAR, "VH-101", "Tesla Model 3", FuelType.ELECTRIC, 85.00, 5));
        fleet.add(VehicleFactory.createVehicle(VehicleType.CAR, "VH-102", "Toyota Camry", FuelType.HYBRID, 55.00, 5));
        fleet.add(VehicleFactory.createVehicle(VehicleType.TRUCK, "VH-201", "Ford F-150", FuelType.DIESEL, 95.00, 3.5));
        fleet.add(VehicleFactory.createVehicle(VehicleType.TRUCK, "VH-202", "Volvo FH16", FuelType.DIESEL, 160.00, 10.0));
        fleet.add(VehicleFactory.createVehicle(VehicleType.MOTORCYCLE, "VH-301", "Yamaha MT-07", FuelType.PETROL, 40.00, 689));
    }

    public void run() {
        System.out.println("=================================================================");
        System.out.println("     DATHEON - ENTERPRISE VEHICLE DISPATCH & RENTAL PLATFORM    ");
        System.out.println("=================================================================");

        boolean exit = false;
        while (!exit) {
            printMenu();
            System.out.print("Select an option (1-7): ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    displayFleetTable(fleet);
                    break;
                case "2":
                    filterFleetWithStreams();
                    break;
                case "3":
                    dispatchVehicle();
                    break;
                case "4":
                    returnVehicle();
                    break;
                case "5":
                    checkFleetMaintenance();
                    break;
                case "6":
                    addNewVehicleViaFactory();
                    break;
                case "7":
                    exit = true;
                    System.out.println("\nExiting Enterprise Vehicle Dispatch Platform. Safe Driving!");
                    break;
                default:
                    System.out.println("[Error] Invalid selection. Choose 1 to 7.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n--------------------- FLEET OPERATIONS ---------------------");
        System.out.println("1. View Complete Vehicle Fleet");
        System.out.println("2. Filter Fleet (Java 8 Streams: Fuel & Price Criteria)");
        System.out.println("3. Dispatch / Rent a Vehicle (With Strategy Pricing)");
        System.out.println("4. Return a Vehicle & Log Mileage");
        System.out.println("5. Vehicle Maintenance & Telemetry Audit");
        System.out.println("6. Commission New Vehicle to Fleet (Factory Pattern)");
        System.out.println("7. Exit");
        System.out.println("------------------------------------------------------------");
    }

    private void displayFleetTable(List<Vehicle> vehicles) {
        if (vehicles.isEmpty()) {
            System.out.println("[Info] No vehicles match the selected criteria.");
            return;
        }

        System.out.println("\n" + "=".repeat(108));
        System.out.println("                                ENTERPRISE VEHICLE FLEET INVENTORY                               ");
        System.out.println("=".repeat(108));
        System.out.printf("| %-8s | %-12s | %-18s | %-9s | %-11s | %-10s | %-28s |%n",
                "ID", "Type", "Model", "Fuel", "Daily Rate", "Status", "Specifications");
        System.out.println("-".repeat(108));

        for (Vehicle v : vehicles) {
            String status = v.isRented() ? "RENTED" : "AVAILABLE";
            System.out.printf("| %-8s | %-12s | %-18s | %-9s | $%-10.2f | %-10s | %-28s |%n",
                    v.getVehicleId(), v.getVehicleType(), v.getModel(), v.getFuelType(),
                    v.getBaseDailyRate(), status, v.getSpecifications());
        }
        System.out.println("=".repeat(108));
    }

    private void filterFleetWithStreams() {
        System.out.println("\n--- STREAM FILTERING ---");
        System.out.println("1. Filter Available Vehicles Only");
        System.out.println("2. Filter by Fuel Type (e.g. ELECTRIC, HYBRID, DIESEL, PETROL)");
        System.out.println("3. Filter by Maximum Daily Budget");
        System.out.print("Select filter type (1-3): ");
        String filterChoice = scanner.nextLine().trim();

        List<Vehicle> filtered;
        switch (filterChoice) {
            case "1":
                // Java Streams: filter by availability
                filtered = fleet.stream()
                        .filter(v -> !v.isRented())
                        .collect(Collectors.toList());
                System.out.println("\n[Stream Filter] Showing all currently available vehicles:");
                displayFleetTable(filtered);
                break;

            case "2":
                System.out.print("Enter Fuel Type (PETROL, DIESEL, ELECTRIC, HYBRID): ");
                String fuelStr = scanner.nextLine().trim().toUpperCase();
                try {
                    FuelType targetFuel = FuelType.valueOf(fuelStr);
                    // Java Streams: filter by fuel type
                    filtered = fleet.stream()
                            .filter(v -> v.getFuelType() == targetFuel)
                            .collect(Collectors.toList());
                    System.out.printf("%n[Stream Filter] Showing vehicles with fuel type: %s%n", targetFuel);
                    displayFleetTable(filtered);
                } catch (IllegalArgumentException e) {
                    System.out.println("[Error] Unknown Fuel Type: " + fuelStr);
                }
                break;

            case "3":
                System.out.print("Enter Maximum Daily Budget ($): ");
                try {
                    double maxBudget = Double.parseDouble(scanner.nextLine().trim());
                    // Java Streams: filter by price & sort ascending
                    filtered = fleet.stream()
                            .filter(v -> v.getBaseDailyRate() <= maxBudget)
                            .sorted(Comparator.comparingDouble(Vehicle::getBaseDailyRate))
                            .collect(Collectors.toList());
                    System.out.printf("%n[Stream Filter] Vehicles under $%.2f/day (Sorted by Rate):%n", maxBudget);
                    displayFleetTable(filtered);
                } catch (NumberFormatException e) {
                    System.out.println("[Error] Please enter a valid numerical budget amount.");
                }
                break;

            default:
                System.out.println("[Error] Invalid filter option.");
        }
    }

    private void dispatchVehicle() {
        System.out.print("\nEnter Vehicle ID to Rent (e.g. VH-101): ");
        String id = scanner.nextLine().trim();

        Vehicle vehicle = findVehicle(id);
        if (vehicle == null) {
            System.out.println("[Error] Vehicle ID '" + id + "' not found.");
            return;
        }

        if (vehicle.isRented()) {
            System.out.println("[Unavailable] Vehicle '" + vehicle.getModel() + "' is already currently rented out.");
            return;
        }

        System.out.print("Enter Rental Duration (Number of Days): ");
        int days;
        try {
            days = Integer.parseInt(scanner.nextLine().trim());
            if (days <= 0) {
                System.out.println("[Error] Rental duration must be at least 1 day.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("[Error] Invalid number of days.");
            return;
        }

        System.out.println("\nSelect Pricing Strategy:");
        pricingStrategies.forEach((k, v) -> System.out.printf("  %s. %s%n", k, v.getStrategyName()));
        System.out.print("Choose strategy (1-3): ");
        String stratChoice = scanner.nextLine().trim();

        PricingStrategy strategy = pricingStrategies.getOrDefault(stratChoice, new StandardPricingStrategy());
        double totalPrice = strategy.calculatePrice(vehicle.getBaseDailyRate(), days);

        vehicle.rentVehicle();

        System.out.println("\n[Dispatch Approved]");
        System.out.printf("  Vehicle Dispatched : %s (%s - %s)%n", vehicle.getModel(), vehicle.getVehicleType(), vehicle.getFuelType());
        System.out.printf("  Base Daily Rate    : $%.2f / day%n", vehicle.getBaseDailyRate());
        System.out.printf("  Rental Period      : %d Days%n", days);
        System.out.printf("  Applied Strategy   : %s%n", strategy.getStrategyName());
        System.out.printf("  TOTAL RENTAL COST  : $%.2f%n", totalPrice);
    }

    private void returnVehicle() {
        System.out.print("\nEnter Vehicle ID to Return (e.g. VH-101): ");
        String id = scanner.nextLine().trim();

        Vehicle vehicle = findVehicle(id);
        if (vehicle == null) {
            System.out.println("[Error] Vehicle ID not found.");
            return;
        }

        if (!vehicle.isRented()) {
            System.out.println("[Notice] Vehicle is not currently marked as rented.");
            return;
        }

        System.out.print("Enter Miles Driven during rental: ");
        int miles;
        try {
            miles = Integer.parseInt(scanner.nextLine().trim());
            if (miles < 0) miles = 0;
        } catch (NumberFormatException e) {
            miles = 0;
        }

        vehicle.returnVehicle();
        vehicle.updateMileage(miles);

        System.out.println("\n[Vehicle Returned Successfully]");
        System.out.println("  Model               : " + vehicle.getModel());
        System.out.println("  Updated Odometer    : " + vehicle.getOdometerMileage() + " miles");
        if (vehicle.requiresService()) {
            System.out.println("  [MAINTENANCE ALERT] Vehicle is due for scheduled inspection & oil change!");
        } else {
            System.out.println("  Telemetry Status    : Fleet condition optimal.");
        }
    }

    private void checkFleetMaintenance() {
        System.out.println("\n" + "=".repeat(85));
        System.out.println("                      FLEET TELEMETRY & MAINTENANCE AUDIT                        ");
        System.out.println("=".repeat(85));
        System.out.printf("| %-8s | %-18s | %-12s | %-14s | %-18s |%n",
                "ID", "Model", "Odometer", "Last Service", "Service Required?");
        System.out.println("-".repeat(85));

        for (Vehicle v : fleet) {
            String alert = v.requiresService() ? "YES - SCHEDULE NOW" : "NO - HEALTHY";
            System.out.printf("| %-8s | %-18s | %,7d mi  | %-14s | %-18s |%n",
                    v.getVehicleId(), v.getModel(), v.getOdometerMileage(),
                    v.getLastServiceDate(), alert);
        }
        System.out.println("=".repeat(85));
    }

    private void addNewVehicleViaFactory() {
        try {
            System.out.println("\n--- COMMISSION VEHICLE (FACTORY PATTERN) ---");
            System.out.print("Select Type (1. CAR, 2. TRUCK, 3. MOTORCYCLE): ");
            String t = scanner.nextLine().trim();
            VehicleType type = (t.equals("2")) ? VehicleType.TRUCK : (t.equals("3")) ? VehicleType.MOTORCYCLE : VehicleType.CAR;

            System.out.print("Enter Unique Vehicle ID (e.g. VH-401): ");
            String id = scanner.nextLine().trim();

            System.out.print("Enter Model Name (e.g. Hyundai Ioniq 5): ");
            String model = scanner.nextLine().trim();

            System.out.print("Select Fuel (1. PETROL, 2. DIESEL, 3. ELECTRIC, 4. HYBRID): ");
            String f = scanner.nextLine().trim();
            FuelType fuel = (f.equals("2")) ? FuelType.DIESEL : (f.equals("3")) ? FuelType.ELECTRIC : (f.equals("4")) ? FuelType.HYBRID : FuelType.PETROL;

            System.out.print("Enter Base Daily Rate ($): ");
            double rate = Double.parseDouble(scanner.nextLine().trim());

            Object extraParam;
            if (type == VehicleType.CAR) {
                System.out.print("Enter Seating Capacity: ");
                extraParam = Integer.parseInt(scanner.nextLine().trim());
            } else if (type == VehicleType.TRUCK) {
                System.out.print("Enter Payload Capacity (Tons): ");
                extraParam = Double.parseDouble(scanner.nextLine().trim());
            } else {
                System.out.print("Enter Engine CC: ");
                extraParam = Integer.parseInt(scanner.nextLine().trim());
            }

            // Invoking Factory Pattern
            Vehicle newVehicle = VehicleFactory.createVehicle(type, id, model, fuel, rate, extraParam);
            fleet.add(newVehicle);

            System.out.printf("%n[Factory Success] '%s' (%s) instantiated and commissioned to fleet!%n",
                    newVehicle.getModel(), newVehicle.getSpecifications());
        } catch (Exception e) {
            System.out.println("[Commission Error] " + e.getMessage());
        }
    }

    private Vehicle findVehicle(String id) {
        return fleet.stream()
                .filter(v -> v.getVehicleId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }
}
