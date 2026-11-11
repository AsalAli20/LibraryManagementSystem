import java.util.ArrayList;

public class Library {
    private ArrayList<Book> books;

    public Library() {
        this.books = new ArrayList<>();
    }

    public boolean addBook(Book book) {
        Book existingBook = findBook(book.getId());

        if (existingBook != null) return false;
        if (book.getId() <= 0) return false;

        books.add(book);
        return true;
    }

    public void showBooks() {
        for (Book book : books) {
            System.out.println("ID: " + book.getId());
            System.out.println("Title: " + book.getTitle());
            System.out.println("Author: " + book.getAuthorName());

            if (book.getAvailable())
                System.out.println("Status: Available");
            else
                System.out.println("Status: Borrowed");

            System.out.println("--------------------");
        }
    }

    public void showAvailableBooks() {
        for (Book book : books) {
            if (book.getAvailable())
                System.out.println(book.getTitle());
        }
    }

    public void showBorrowedBooks() {
        boolean borrowed = false;

        for (Book book : books) {
            if (!book.getAvailable()) {
                System.out.println(book.getTitle());
                borrowed = true;
            }
        }

        if (!borrowed)
            System.out.println("No borrowed books");
    }

    public Book findBook(int id) {
        for (Book book : books) {
            if (book.getId() == id)
                return book;
        }
        return null;
    }

    public Book findBookByTitle(String title) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title.trim()))
                return book;
        }
        return null;
    }

    public void deleteBook(int id) {
        Book foundBook = findBook(id);

        if (foundBook != null)
            books.remove(foundBook);
    }

    public void borrowBook(int id) {
        Book foundBook = findBook(id);

        if (foundBook != null)
            foundBook.borrowBook();
        else
            System.out.println("Book not found");
    }

    public void returnBook(int id) {
        Book foundBook = findBook(id);

        if (foundBook != null)
            foundBook.returnBook();
        else
            System.out.println("Book not found");
    }

    public int countBooks() {
        int count = 0;

        for (Book book : books)
            count++;

        return count;
    }

    public int countAvailableBooks() {
        int count = 0;

        for (Book book : books) {
            if (book.getAvailable())
                count++;
        }

        return count;
    }

    public int countBorrowedBooks() {
        int count = 0;

        for (Book book : books) {
            if (!book.getAvailable())
                count++;
        }

        return count;
    }

    public boolean isEmpty() {
        return books.isEmpty();
    }
}