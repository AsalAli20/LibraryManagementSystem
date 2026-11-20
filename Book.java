

public class Book {
    private final int id;
    private final String title;
    private final String authorName;
    private int totalCopies;
    private int availableCopies;

    public Book(int id, String title, String authorName, int totalCopies) {
        this(id, title, authorName, totalCopies, totalCopies);
    }

    public Book(int id, String title, String authorName, int totalCopies, int availableCopies) {
        if (id <= 0 || title == null || title.isBlank() || authorName == null || authorName.isBlank()
                || totalCopies <= 0 || availableCopies < 0 || availableCopies > totalCopies) {
            throw new IllegalArgumentException("Invalid book details");
        }
        this.id = id;
        this.title = title.trim();
        this.authorName = authorName.trim();
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthorName() { return authorName; }
    public int getTotalCopies() { return totalCopies; }
    public int getAvailableCopies() { return availableCopies; }
    public boolean isAvailable() { return availableCopies > 0; }

    public boolean borrowBook() {
        if (availableCopies == 0) return false;
        availableCopies--;
        return true;
    }

    public boolean returnBook() {
        if (availableCopies >= totalCopies) return false;
        availableCopies++;
        return true;
    }

    public void addCopies(int count) {
        if (count <= 0) throw new IllegalArgumentException("Count must be positive");
        totalCopies += count;
        availableCopies += count;
    }

    @Override
    public String toString() {
        return "ID: " + id + " | " + title + " | Author: " + authorName
                + " | Available: " + availableCopies + "/" + totalCopies;
    }
}
