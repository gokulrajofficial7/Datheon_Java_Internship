/**
 * Models a Book entity in the library catalog demonstrating OOP Encapsulation.
 */
public class Book {
    private final String isbn;
    private String title;
    private String author;
    private String genre;
    private int year;
    private int copiesAvailable;

    public Book(String isbn, String title, String author, String genre, int year, int copiesAvailable) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be empty.");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Book title cannot be empty.");
        }
        if (copiesAvailable < 0) {
            throw new IllegalArgumentException("Copies available cannot be negative.");
        }
        this.isbn = isbn.trim();
        this.title = title.trim();
        this.author = (author != null) ? author.trim() : "Unknown";
        this.genre = (genre != null) ? genre.trim() : "General";
        this.year = year;
        this.copiesAvailable = copiesAvailable;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title != null && !title.trim().isEmpty()) {
            this.title = title.trim();
        }
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getCopiesAvailable() {
        return copiesAvailable;
    }

    public boolean isAvailable() {
        return copiesAvailable > 0;
    }

    public synchronized boolean borrowCopy() {
        if (copiesAvailable > 0) {
            copiesAvailable--;
            return true;
        }
        return false;
    }

    public synchronized void returnCopy() {
        copiesAvailable++;
    }
}
