// Book class for Library Management System

public class Book {
    private int id;
    private String title;
    private String authorName;
    private boolean available;

    public Book(int id, String title, String authorName) {
        this.id = id;
        this.title = title;
        this.authorName = authorName;
        this.available = true;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthorName() {
        return authorName;
    }

    public boolean getAvailable() {
        return available;
    }

    public void borrowBook() {
        if (available) {
            available = false;
            System.out.println("Book borrowed successfully");
        } else {
            System.out.println("This book is already borrowed");
        }
    }

    public void returnBook() {
        if (!available) {
            available = true;
            System.out.println("Book returned successfully");
        } else {
            System.out.println("This book is already available");
        }
    }
}