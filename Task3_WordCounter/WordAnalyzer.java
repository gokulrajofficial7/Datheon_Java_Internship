import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * High-performance text analysis and corpus processing utility.
 * Demonstrates Java IO (BufferedReader, Files), Java Collections (HashMap, List),
 * regular expressions, tokenization, and statistical metric reporting.
 */
public class WordAnalyzer {

    // Common English stop words for optional content analysis
    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
            "a", "about", "above", "after", "again", "against", "all", "am", "an", "and", "any", "are", "as",
            "at", "be", "because", "been", "before", "being", "below", "between", "both", "but", "by", "could",
            "did", "do", "does", "doing", "down", "during", "each", "few", "for", "from", "further", "had",
            "has", "have", "having", "he", "her", "here", "hers", "herself", "him", "himself", "his", "how",
            "i", "if", "in", "into", "is", "it", "its", "itself", "just", "me", "more", "most", "my", "myself",
            "no", "nor", "not", "now", "of", "off", "on", "once", "only", "or", "other", "ought", "our", "ours",
            "ourselves", "out", "over", "own", "same", "she", "should", "so", "some", "such", "than", "that",
            "the", "their", "theirs", "them", "themselves", "then", "there", "these", "they", "this", "those",
            "through", "to", "too", "under", "until", "up", "very", "was", "we", "were", "what", "when", "where",
            "which", "while", "who", "whom", "why", "with", "would", "you", "your", "yours", "yourself", "yourselves"
    ));

    private String rawText;
    private final List<String> sentences;
    private final List<String> wordTokens;
    private final Map<String, Integer> frequencyMap;
    private final Scanner scanner;

    public WordAnalyzer() {
        this.rawText = "";
        this.sentences = new ArrayList<>();
        this.wordTokens = new ArrayList<>();
        this.frequencyMap = new HashMap<>();
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        WordAnalyzer analyzer = new WordAnalyzer();
        analyzer.run();
    }

    public void run() {
        System.out.println("=================================================================");
        System.out.println("       DATHEON - WORD COUNTER & CORPUS ANALYZER UTILITY          ");
        System.out.println("=================================================================");

        // Automatically load default starter file if present
        File defaultFile = new File("sample_article.txt");
        if (defaultFile.exists()) {
            loadFromFile(defaultFile.getAbsolutePath());
        }

        boolean exit = false;
        while (!exit) {
            printMenu();
            System.out.print("Select an option (1-6): ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    displayFullAnalysisReport();
                    break;
                case "2":
                    displayTopKeywordsTable(10);
                    break;
                case "3":
                    searchKeywordOccurrence();
                    break;
                case "4":
                    loadCustomFile();
                    break;
                case "5":
                    exportReportToFile();
                    break;
                case "6":
                    exit = true;
                    System.out.println("\nExiting Corpus Analyzer. Happy Coding!");
                    break;
                default:
                    System.out.println("[Error] Invalid selection. Choose 1 to 6.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n----------------------- MAIN MENU -----------------------");
        System.out.println("1. View Complete Corpus Metrics & Statistics");
        System.out.println("2. Display Top 10 Most Frequent Keywords (Table)");
        System.out.println("3. Search Keyword Frequency & Context Sentences");
        System.out.println("4. Ingest New Text File");
        System.out.println("5. Export Analysis Report to File");
        System.out.println("6. Exit");
        System.out.println("---------------------------------------------------------");
    }

    public void loadFromFile(String filePath) {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            System.out.println("[Warning] File not found: " + filePath);
            return;
        }

        try {
            String content = Files.readString(path);
            processText(content);
            System.out.printf("[File Ingested] Successfully analyzed '%s' (%d bytes)%n", path.getFileName(), Files.size(path));
        } catch (IOException e) {
            System.out.println("[Error] Failed to read file: " + e.getMessage());
        }
    }

    public void processText(String text) {
        this.rawText = (text != null) ? text.trim() : "";
        this.sentences.clear();
        this.wordTokens.clear();
        this.frequencyMap.clear();

        if (this.rawText.isEmpty()) return;

        // Parse sentences using regex delimiters [.!?]+
        String[] sentenceArray = rawText.split("(?<=[.!?])\\s+");
        for (String s : sentenceArray) {
            if (!s.trim().isEmpty()) {
                sentences.add(s.trim());
            }
        }

        // Tokenize words using non-word regex \W+ with case folding
        String[] tokens = rawText.toLowerCase().split("[^a-zA-Z0-9'-]+");
        for (String t : tokens) {
            String cleaned = t.replaceAll("^'+|'+$", ""); // Strip boundary apostrophes
            if (!cleaned.isEmpty()) {
                wordTokens.add(cleaned);
                frequencyMap.put(cleaned, frequencyMap.getOrDefault(cleaned, 0) + 1);
            }
        }
    }

    private void displayFullAnalysisReport() {
        if (wordTokens.isEmpty()) {
            System.out.println("[Info] No text loaded. Please load a file or enter text first.");
            return;
        }

        int totalWords = wordTokens.size();
        int uniqueWords = frequencyMap.size();
        int totalCharsWithSpaces = rawText.length();
        int totalCharsWithoutSpaces = rawText.replaceAll("\\s+", "").length();
        int sentenceCount = sentences.size();
        double avgSentenceLength = (sentenceCount > 0) ? (double) totalWords / sentenceCount : 0.0;

        int totalWordCharacters = 0;
        for (String w : wordTokens) {
            totalWordCharacters += w.length();
        }
        double avgWordLength = (totalWords > 0) ? (double) totalWordCharacters / totalWords : 0.0;
        double lexicalDiversity = (totalWords > 0) ? ((double) uniqueWords / totalWords) * 100.0 : 0.0;

        System.out.println("\n=================================================================");
        System.out.println("                 CORPUS STATISTICAL METRICS                      ");
        System.out.println("=================================================================");
        System.out.printf(" Total Word Count (Tokens)      : %,d%n", totalWords);
        System.out.printf(" Unique Words (Vocabulary)      : %,d%n", uniqueWords);
        System.out.printf(" Lexical Diversity Rate         : %.2f%% (Vocabulary Richness)%n", lexicalDiversity);
        System.out.printf(" Total Characters (With Spaces) : %,d%n", totalCharsWithSpaces);
        System.out.printf(" Total Characters (No Spaces)   : %,d%n", totalCharsWithoutSpaces);
        System.out.printf(" Total Sentences Count          : %,d%n", sentenceCount);
        System.out.printf(" Average Sentence Length        : %.2f words / sentence%n", avgSentenceLength);
        System.out.printf(" Average Word Length            : %.2f characters / word%n", avgWordLength);
        System.out.println("=================================================================");
    }

    private void displayTopKeywordsTable(int limit) {
        if (frequencyMap.isEmpty()) {
            System.out.println("[Info] No words to analyze.");
            return;
        }

        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(frequencyMap.entrySet());
        // Sort descending by count, then alphabetically
        sortedEntries.sort((a, b) -> {
            int cmp = b.getValue().compareTo(a.getValue());
            return (cmp != 0) ? cmp : a.getKey().compareTo(b.getKey());
        });

        int totalWords = wordTokens.size();
        int countToDisplay = Math.min(limit, sortedEntries.size());

        System.out.println("\n" + "=".repeat(75));
        System.out.printf("                TOP %d MOST FREQUENT WORDS DISTRIBUTION              %n", countToDisplay);
        System.out.println("=".repeat(75));
        System.out.printf("| %-4s | %-18s | %-10s | %-12s | %-15s |%n", "Rank", "Word", "Frequency", "Density (%)", "Category");
        System.out.println("-".repeat(75));

        for (int i = 0; i < countToDisplay; i++) {
            Map.Entry<String, Integer> entry = sortedEntries.get(i);
            String word = entry.getKey();
            int freq = entry.getValue();
            double density = (totalWords > 0) ? ((double) freq / totalWords) * 100.0 : 0.0;
            String category = STOP_WORDS.contains(word) ? "Stop Word" : "Key Content";

            System.out.printf("| %-4d | %-18s | %-10d | %-11.2f%% | %-15s |%n",
                    (i + 1), word, freq, density, category);
        }
        System.out.println("=".repeat(75));
    }

    private void searchKeywordOccurrence() {
        if (wordTokens.isEmpty()) {
            System.out.println("[Info] Corpus is empty. Please load text first.");
            return;
        }

        System.out.print("\nEnter keyword to search: ");
        String query = scanner.nextLine().trim().toLowerCase();

        int freq = frequencyMap.getOrDefault(query, 0);
        System.out.println("\n---------------- SEARCH RESULTS ----------------");
        System.out.printf("Keyword          : '%s'%n", query);
        System.out.printf("Total Occurrences: %d%n", freq);

        if (freq > 0) {
            System.out.println("\nContext Sentences:");
            int matchCount = 0;
            for (String s : sentences) {
                if (s.toLowerCase().matches(".*\\b" + query + "\\b.*")) {
                    matchCount++;
                    System.out.printf("  [%d] \"%s\"%n", matchCount, s);
                }
            }
        }
        System.out.println("------------------------------------------------");
    }

    private void loadCustomFile() {
        System.out.print("\nEnter file path (or press Enter for 'sample_article.txt'): ");
        String path = scanner.nextLine().trim();
        if (path.isEmpty()) {
            path = "sample_article.txt";
        }
        loadFromFile(path);
    }

    private void exportReportToFile() {
        if (wordTokens.isEmpty()) {
            System.out.println("[Info] Nothing to export. Load text first.");
            return;
        }

        String exportFileName = "analysis_report.txt";
        try (PrintWriter writer = new PrintWriter(new FileWriter(exportFileName))) {
            writer.println("=================================================================");
            writer.println("          TEXT CORPUS ANALYSIS & WORD DISTRIBUTION REPORT         ");
            writer.println("=================================================================");
            writer.printf("Total Words (Tokens)      : %d%n", wordTokens.size());
            writer.printf("Unique Words (Vocabulary) : %d%n", frequencyMap.size());
            writer.printf("Total Sentences           : %d%n", sentences.size());
            writer.printf("Average Sentence Length   : %.2f words%n", (double) wordTokens.size() / sentences.size());
            writer.println("-----------------------------------------------------------------");
            writer.println("TOP 10 KEYWORDS:");
            List<Map.Entry<String, Integer>> sorted = new ArrayList<>(frequencyMap.entrySet());
            sorted.sort((a, b) -> b.getValue().compareTo(a.getValue()));

            for (int i = 0; i < Math.min(10, sorted.size()); i++) {
                writer.printf("%2d. %-15s : %d occurrences%n", (i + 1), sorted.get(i).getKey(), sorted.get(i).getValue());
            }
            writer.println("=================================================================");
            System.out.println("[Export Complete] Statistical report written to '" + exportFileName + "'.");
        } catch (IOException e) {
            System.out.println("[Error] Failed to write report file: " + e.getMessage());
        }
    }
}
