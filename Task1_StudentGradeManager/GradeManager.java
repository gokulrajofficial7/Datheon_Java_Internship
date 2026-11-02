import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Console application to manage student cohort records, compute statistical
 * metrics (average, median, GPA), and render formatted academic report cards.
 */
public class GradeManager {
    private final List<Student> cohort;
    private final Scanner scanner;

    public GradeManager() {
        this.cohort = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        GradeManager app = new GradeManager();
        app.run();
    }

    public void run() {
        System.out.println("===============================================================");
        System.out.println("    DATHEON - STUDENT GRADE MANAGEMENT SYSTEM (OOP)           ");
        System.out.println("===============================================================");

        // Automatically load dataset if available in current directory
        File defaultFile = new File("sample_student_grades.csv");
        if (defaultFile.exists()) {
            loadFromCSV(defaultFile.getAbsolutePath());
        }

        boolean exit = false;
        while (!exit) {
            printMenu();
            System.out.print("Select an option (1-6): ");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    registerStudent();
                    break;
                case "2":
                    recordStudentMarks();
                    break;
                case "3":
                    loadCSVInteractive();
                    break;
                case "4":
                    computeClassAnalytics();
                    break;
                case "5":
                    generateReportCards();
                    break;
                case "6":
                    exit = true;
                    System.out.println("\nExiting Student Grade Management System. Happy Coding!");
                    break;
                default:
                    System.out.println("[Error] Invalid selection. Please choose an option between 1 and 6.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n----------------- MAIN MENU -----------------");
        System.out.println("1. Register New Student");
        System.out.println("2. Record / Update Course Scores");
        System.out.println("3. Load Student Data from CSV");
        System.out.println("4. Compute Class Metrics (Average, Median, Topper)");
        System.out.println("5. Generate Tabular Academic Report Cards");
        System.out.println("6. Exit");
        System.out.println("---------------------------------------------");
    }

    private void registerStudent() {
        try {
            System.out.print("Enter Student ID: ");
            int id = Integer.parseInt(scanner.nextLine().trim());

            if (findStudentById(id) != null) {
                System.out.println("[Error] Student with ID " + id + " already exists.");
                return;
            }

            System.out.print("Enter Student Full Name: ");
            String name = scanner.nextLine().trim();

            Student student = new Student(id, name);
            cohort.add(student);
            System.out.println("[Success] Student '" + name + "' (ID: " + id + ") registered successfully.");
        } catch (NumberFormatException e) {
            System.out.println("[Error] Invalid Student ID. Please enter a valid integer.");
        } catch (IllegalArgumentException e) {
            System.out.println("[Validation Error] " + e.getMessage());
        }
    }

    private void recordStudentMarks() {
        if (cohort.isEmpty()) {
            System.out.println("[Info] No students registered yet. Please add students first.");
            return;
        }

        try {
            System.out.print("Enter Student ID: ");
            int id = Integer.parseInt(scanner.nextLine().trim());

            Student student = findStudentById(id);
            if (student == null) {
                System.out.println("[Error] Student with ID " + id + " not found.");
                return;
            }

            System.out.print("Enter Subject / Course Name (e.g. Math, Science): ");
            String subject = scanner.nextLine().trim();

            System.out.print("Enter Score (0 - 100): ");
            double score = Double.parseDouble(scanner.nextLine().trim());

            student.addOrUpdateGrade(subject, score);
            System.out.println("[Success] Recorded " + score + " in " + subject + " for " + student.getName() + ".");
        } catch (NumberFormatException e) {
            System.out.println("[Error] Invalid number format. Score must be a valid decimal number.");
        } catch (IllegalArgumentException e) {
            System.out.println("[Validation Error] " + e.getMessage());
        }
    }

    private void loadCSVInteractive() {
        System.out.print("Enter CSV File Path (or press Enter for 'sample_student_grades.csv'): ");
        String path = scanner.nextLine().trim();
        if (path.isEmpty()) {
            path = "sample_student_grades.csv";
        }
        loadFromCSV(path);
    }

    public void loadFromCSV(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("[Warning] CSV file not found at: " + filePath);
            return;
        }

        int loadedCount = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String headerLine = br.readLine();
            if (headerLine == null) {
                System.out.println("[Warning] CSV file is empty.");
                return;
            }

            String[] headers = headerLine.split(",");
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] values = line.split(",");
                if (values.length < 2) continue;

                int id = Integer.parseInt(values[0].trim());
                String name = values[1].trim();

                Student student = findStudentById(id);
                if (student == null) {
                    student = new Student(id, name);
                    cohort.add(student);
                }

                // Parse subject columns
                for (int i = 2; i < values.length && i < headers.length; i++) {
                    String subjectRaw = headers[i].trim().replace("_score", "");
                    String subject = subjectRaw.substring(0, 1).toUpperCase() + subjectRaw.substring(1);
                    double score = Double.parseDouble(values[i].trim());
                    student.addOrUpdateGrade(subject, score);
                }
                loadedCount++;
            }
            System.out.println("[Data Loaded] Successfully ingested " + loadedCount + " student records from " + file.getName());
        } catch (IOException | NumberFormatException e) {
            System.out.println("[Error] Failed to read CSV file: " + e.getMessage());
        }
    }

    private void computeClassAnalytics() {
        if (cohort.isEmpty()) {
            System.out.println("[Info] No students registered in the cohort.");
            return;
        }

        List<Double> averages = new ArrayList<>();
        Student topper = null;
        double highestAvg = -1.0;
        double classTotal = 0.0;
        int evaluatedStudents = 0;

        for (Student s : cohort) {
            if (!s.getGrades().isEmpty()) {
                double avg = s.calculateAverage();
                averages.add(avg);
                classTotal += avg;
                evaluatedStudents++;

                if (avg > highestAvg) {
                    highestAvg = avg;
                    topper = s;
                }
            }
        }

        if (evaluatedStudents == 0) {
            System.out.println("[Info] No grades recorded yet for any student.");
            return;
        }

        Collections.sort(averages);
        double classAverage = classTotal / evaluatedStudents;
        double median;
        int size = averages.size();
        if (size % 2 == 1) {
            median = averages.get(size / 2);
        } else {
            median = (averages.get((size / 2) - 1) + averages.get(size / 2)) / 2.0;
        }

        System.out.println("\n=======================================================");
        System.out.println("              COHORT ACADEMIC ANALYTICS                ");
        System.out.println("=======================================================");
        System.out.printf("Total Enrolled Students : %d%n", cohort.size());
        System.out.printf("Evaluated Students      : %d%n", evaluatedStudents);
        System.out.printf("Overall Class Average   : %.2f%%%n", classAverage);
        System.out.printf("Class Median Average    : %.2f%%%n", median);
        if (topper != null) {
            System.out.printf("Class Top Performer     : %s (ID: %d) with %.2f%% [GPA: %.2f, Grade: %s]%n",
                    topper.getName(), topper.getStudentId(), highestAvg, topper.calculateGPA(), topper.getLetterGrade());
        }
        System.out.println("=======================================================");
    }

    public void generateReportCards() {
        if (cohort.isEmpty()) {
            System.out.println("[Info] No students in cohort to display.");
            return;
        }

        // Collect all distinct subjects dynamically
        Set<String> subjectSet = new LinkedHashSet<>();
        for (Student s : cohort) {
            subjectSet.addAll(s.getGrades().keySet());
        }
        List<String> subjects = new ArrayList<>(subjectSet);

        System.out.println("\n" + "=".repeat(105));
        System.out.println("                       OFFICIAL ACADEMIC GRADE REPORT SHEET                             ");
        System.out.println("=".repeat(105));

        // Build Header
        StringBuilder sbHeader = new StringBuilder();
        sbHeader.append(String.format("| %-6s | %-16s |", "ID", "Student Name"));
        for (String sub : subjects) {
            sbHeader.append(String.format(" %-9s |", sub.length() > 9 ? sub.substring(0, 9) : sub));
        }
        sbHeader.append(String.format(" %-7s | %-8s | %-5s | %-5s |", "Total", "Avg(%)", "GPA", "Grade"));

        String separator = "-".repeat(sbHeader.length());
        System.out.println(separator);
        System.out.println(sbHeader.toString());
        System.out.println(separator);

        // Build Student Rows
        for (Student s : cohort) {
            StringBuilder sbRow = new StringBuilder();
            sbRow.append(String.format("| %-6d | %-16s |", s.getStudentId(), truncate(s.getName(), 16)));

            for (String sub : subjects) {
                Double score = s.getGrade(sub);
                if (score != null) {
                    sbRow.append(String.format(" %-9.1f |", score));
                } else {
                    sbRow.append(String.format(" %-9s |", "N/A"));
                }
            }

            sbRow.append(String.format(" %-7.1f | %-8.2f | %-5.2f | %-5s |",
                    s.calculateTotal(), s.calculateAverage(), s.calculateGPA(), s.getLetterGrade()));
            System.out.println(sbRow.toString());
        }

        System.out.println(separator);
    }

    private Student findStudentById(int id) {
        for (Student s : cohort) {
            if (s.getStudentId() == id) {
                return s;
            }
        }
        return null;
    }

    private String truncate(String text, int maxLen) {
        if (text == null) return "";
        return text.length() <= maxLen ? text : text.substring(0, maxLen - 3) + "...";
    }
}
