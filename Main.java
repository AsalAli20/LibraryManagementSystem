
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Library library = new Library();

        library.addBook(new Book(1, "Java Basics", "Ahmed"));
        library.addBook(new Book(2, "SQL Basics", "Sara"));

        int choice = 0;

        while (choice != 9) {
            System.out.println("\n1. Show books");
            System.out.println("2. Show available books");
            System.out.println("3. Borrow book");
            System.out.println("4. Return book");
            System.out.println("5. Add book");
            System.out.println("6. Delete book");
            System.out.println("7. Show library statistics");
            System.out.println("8. Search book by title");
            System.out.println("9. Exit");

            choice = input.nextInt();

            if (choice == 1) {
                library.showBooks();

            } else if (choice == 2) {
                library.showAvailableBooks();

            } else if (choice == 3) {
                System.out.println("Enter book id:");
                int id = input.nextInt();
                library.borrowBook(id);

            } else if (choice == 4) {
                System.out.println("Enter book id:");
                int id = input.nextInt();
                library.returnBook(id);

            } else if (choice == 5) {
                System.out.println("Enter book id:");
                int newBookId = input.nextInt();
                input.nextLine();

                System.out.println("Enter book title:");
                String newBookTitle = input.nextLine();

                System.out.println("Enter author name:");
                String newBookAuthorName = input.nextLine();

                Book newBook = new Book(
                    newBookId, newBookTitle, newBookAuthorName
                );

               
if (newBookId <= 0) {
    System.out.println("Invalid Book ID");

} else if (library.findBook(newBookId) != null) {
    System.out.println("This Book ID already exists");

} else {
    boolean added = library.addBook(newBook);

    if (added) {
        System.out.println("Book added successfully");
    }
}
                
            } else if (choice == 6) {
                System.out.println("Enter book id:");
                int deleteId = input.nextInt();

                Book foundBook = library.findBook(deleteId);

                if (foundBook != null) {
                    library.deleteBook(deleteId);
                    System.out.println("Book removed successfully");
                } else {
                    System.out.println("Book not found");
                }

            } else if (choice == 7) {
                System.out.println("Total books: " + library.countBooks());
                System.out.println("Available books: " + library.countAvailableBooks());
                System.out.println("Borrowed books: " + library.countBorrowedBooks());

            } else if (choice == 8) {
                input.nextLine();

                System.out.println("Enter book title:");
                String searchTitle = input.nextLine();

                Book foundBook = library.findBookByTitle(searchTitle);

                if (foundBook != null) {
                    System.out.println("Title: " + foundBook.getTitle());
                    System.out.println("Author: " + foundBook.getAuthorName());

                    if (foundBook.getAvailable())
                        System.out.println("Status: Available");
                    else
                        System.out.println("Status: Borrowed");
                } else {
                    System.out.println("Book not found");
                }

            } else if (choice == 9) {
                System.out.println("Program ended");

            } else {
                System.out.println("Invalid choice");
            }
        }

        input.close();
    }
}
