# Task 3: Word Counter & Text Corpus Analyzer Utility

## Overview
A high-performance text analysis and corpus processing utility built with Java IO and the Java Collections Framework. It ingests arbitrary text documents, tokenizes text, calculates statistical linguistic metrics (Total Tokens, Lexical Diversity, Average Sentence Length), and ranks word frequency distributions in a clean tabular view.

---

## 🛠️ Key Technical Features
1. **Java File I/O (`Files`, `BufferedReader`, `PrintWriter`):**
   - High-throughput file reading using `java.nio.file.Files`.
   - Automated export of analytical findings to `analysis_report.txt`.
2. **Text Normalization & Regular Expressions:**
   - Case folding to lowercase.
   - Non-word boundary tokenization using `[^a-zA-Z0-9'-]+`.
   - Sentence boundary detection via lookbehind regex `(?<=[.!?])\s+`.
3. **Java Collections Framework:**
   - `HashMap<String, Integer>` for $O(1)$ keyword frequency aggregation.
   - Entry sorting with custom comparators (`(a, b) -> b.getValue().compareTo(a.getValue())`).
   - Stop-words lookup using `HashSet<String>` for instant content classification.
4. **Corpus Statistical Metrics:**
   - Total Tokens vs. Unique Vocabulary.
   - Lexical Diversity / Vocabulary Richness Rate ($\frac{\text{Unique Words}}{\text{Total Words}} \times 100$).
   - Average Sentence Length & Average Word Length.
   - Keyword context search showing exact matching sentences.

---

## 📁 Project Structure
```text
Task3_WordCounter/
├── sample_article.txt         # Starter text corpus
├── WordAnalyzer.java          # Core processing utility & interactive CLI
├── analysis_report.txt        # Generated analytical report
└── README.md                  # Documentation
```

---

## 🚀 How to Compile & Run

### 1. Compile
```bash
javac WordAnalyzer.java
```

### 2. Execute
```bash
java WordAnalyzer
```

---

## 📊 Sample Output
```text
===========================================================================
                TOP 10 MOST FREQUENT WORDS DISTRIBUTION              
===========================================================================
| Rank | Word               | Frequency  | Density (%)  | Category        |
---------------------------------------------------------------------------
| 1    | and                | 3          | 6.67       % | Stop Word       |
| 2    | data               | 2          | 4.44       % | Key Content     |
| 3    | modern             | 2          | 4.44       % | Key Content     |
| 4    | software           | 2          | 4.44       % | Key Content     |
| 5    | architectures      | 1          | 2.22       % | Key Content     |
| 6    | are                | 1          | 2.22       % | Stop Word       |
| 7    | artificial         | 1          | 2.22       % | Key Content     |
| 8    | automation         | 1          | 2.22       % | Key Content     |
| 9    | build              | 1          | 2.22       % | Key Content     |
| 10   | clean              | 1          | 2.22       % | Key Content     |
===========================================================================
```
