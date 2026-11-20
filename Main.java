import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    private static int readPositiveInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int number = Integer.parseInt(scanner.nextLine().trim());
                if (number > 0) return number;
                System.out.println("ID or number must be positive");
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter digits only.");
            }
        }
    }

    private static String readText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String text = scanner.nextLine().trim();
            if (!text.isEmpty()) return text;
            System.out.println("This field cannot be empty");
        }
    }

    private static void printItems(List<?> items) {
        if (items.isEmpty()) System.out.println("No results found");
        else for (Object item : items) System.out.println(item);
    }

    private static void menu() {
        System.out.println("\n=== LIBRARY MANAGEMENT SYSTEM ===");
        System.out.println("1. Show all books");
        System.out.println("2. Show available books");
        System.out.println("3. Add book");
        System.out.println("4. Delete book");
        System.out.println("5. Search books by title");
        System.out.println("6. Add copies to a book");
        System.out.println("7. Add member");
        System.out.println("8. Show members");
        System.out.println("9. Borrow a book");
        System.out.println("10. Return a book");
        System.out.println("11. Show borrow records");
        System.out.println("12. Library statistics");
        System.out.println("13. Save data");
        System.out.println("0. Save and exit");
    }

    public static void main(String[] args) {
        Library library = new Library(Path.of("data"));
        try {
            library.load();
            System.out.println("Saved data loaded successfully");
        } catch (IOException e) {
            System.out.println("Could not load saved data: " + e.getMessage());
            System.out.println("Existing data files were not changed. Exiting for safety.");
            return;
        }

        while (true) {
            menu();
            System.out.print("Choose an option: ");
            int choice;
            try { choice = Integer.parseInt(scanner.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("Invalid option"); continue; }
            try {
                switch (choice) {
                    case 1 -> printItems(library.getBooks());
                    case 2 -> printItems(library.getAvailableBooks());
                    case 3 -> {
                        int id = readPositiveInt("Book ID: ");
                        String title = readText("Title: ");
                        String author = readText("Author: ");
                        int copies = readPositiveInt("Number of copies: ");
                        library.addBook(new Book(id, title, author, copies));
                        System.out.println("Book added successfully");
                    }
                    case 4 -> {
                        library.deleteBook(readPositiveInt("Book ID to delete: "));
                        System.out.println("Book deleted successfully");
                    }
                    case 5 -> printItems(library.searchByTitle(readText("Search title: ")));
                    case 6 -> {
                        Book book = library.findBook(readPositiveInt("Book ID: "));
                        if (book == null) System.out.println("Book not found");
                        else { book.addCopies(readPositiveInt("Additional copies: ")); System.out.println("Copies added"); }
                    }
                    case 7 -> {
                        library.addMember(new Member(readPositiveInt("Member ID: "), readText("Member name: ")));
                        System.out.println("Member added successfully");
                    }
                    case 8 -> printItems(library.getMembers());
                    case 9 -> {
                        int bookId = readPositiveInt("Book ID: ");
                        int memberId = readPositiveInt("Member ID: ");
                        BorrowRecord record = library.borrow(bookId, memberId);
                        System.out.println("Borrow successful! Your Borrow ID is: " + record.getId());
                    }
                    case 10 -> {
                        library.returnBorrow(readPositiveInt("Borrow ID: "));
                        System.out.println("Book returned successfully");
                    }
                    case 11 -> printItems(library.getRecords());
                    case 12 -> System.out.println(library.statistics());
                    case 13 -> { library.save(); System.out.println("Data saved successfully"); }
                    case 0 -> {
                        library.save();
                        System.out.println("Data saved. Goodbye!");
                        return;
                    }
                    default -> System.out.println("Unknown option");
                }
            } catch (IllegalArgumentException | IllegalStateException | IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
