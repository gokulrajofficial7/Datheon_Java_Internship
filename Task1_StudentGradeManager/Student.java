import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents a student academic profile demonstrating OOP Encapsulation and Data Validation.
 */
public class Student {
    private int studentId;
    private String name;
    private final Map<String, Double> grades; // Maps course/subject name to score

    public Student(int studentId, String name) {
        if (studentId <= 0) {
            throw new IllegalArgumentException("Student ID must be a positive number.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Student name cannot be empty.");
        }
        this.studentId = studentId;
        this.name = name.trim();
        this.grades = new LinkedHashMap<>();
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        if (studentId <= 0) {
            throw new IllegalArgumentException("Student ID must be a positive number.");
        }
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Student name cannot be empty.");
        }
        this.name = name.trim();
    }

    public Map<String, Double> getGrades() {
        return Collections.unmodifiableMap(grades);
    }

    /**
     * Encapsulated score assignment with validation enforcing 0.0 <= score <= 100.0
     */
    public void addOrUpdateGrade(String subject, double score) {
        if (subject == null || subject.trim().isEmpty()) {
            throw new IllegalArgumentException("Subject name cannot be empty.");
        }
        if (score < 0.0 || score > 100.0) {
            throw new IllegalArgumentException("Score must be between 0 and 100. Provided: " + score);
        }
        grades.put(subject.trim(), score);
    }

    public double calculateTotal() {
        return grades.values().stream().mapToDouble(Double::doubleValue).sum();
    }

    public double calculateAverage() {
        if (grades.isEmpty()) {
            return 0.0;
        }
        return calculateTotal() / grades.size();
    }

    /**
     * Converts numerical percentage average to GPA on a standard 4.0 scale.
     */
    public double calculateGPA() {
        double avg = calculateAverage();
        if (avg >= 90.0) return 4.0;
        if (avg >= 80.0) return 3.5;
        if (avg >= 70.0) return 3.0;
        if (avg >= 60.0) return 2.5;
        if (avg >= 50.0) return 2.0;
        return 0.0;
    }

    /**
     * Determines academic letter grade.
     */
    public String getLetterGrade() {
        double avg = calculateAverage();
        if (avg >= 90.0) return "A+";
        if (avg >= 80.0) return "A";
        if (avg >= 70.0) return "B";
        if (avg >= 60.0) return "C";
        if (avg >= 50.0) return "D";
        return "F";
    }

    public Double getGrade(String subject) {
        return grades.get(subject);
    }
}
