
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class Library {
    private final ArrayList<Book> books = new ArrayList<>();
    private final ArrayList<Member> members = new ArrayList<>();
    private final ArrayList<BorrowRecord> records = new ArrayList<>();
    private int nextBorrowId = 1;
    private final Path dataFolder;

    public Library(Path dataFolder) {
        this.dataFolder = dataFolder;
    }

    public void addBook(Book book) {
        if (findBook(book.getId()) != null) {
            throw new IllegalArgumentException("This Book ID already exists");
        }
        books.add(book);
    }

    public Book findBook(int id) {
        for (Book book : books) {
            if (book.getId() == id) return book;
        }
        return null;
    }

    public void deleteBook(int id) {
        Book book = findBook(id);
        if (book == null) {
            throw new IllegalArgumentException("Book not found");
        }

        for (BorrowRecord record : records) {
            if (record.getBookId() == id && record.isActive()) {
                throw new IllegalStateException(
                    "Cannot delete a book with active loans"
                );
            }
        }
        books.remove(book);
    }

    public List<Book> getBooks() {
        return new ArrayList<>(books);
    }

    public List<Book> getAvailableBooks() {
        ArrayList<Book> result = new ArrayList<>();
        for (Book book : books) {
            if (book.isAvailable()) result.add(book);
        }
        return result;
    }

    public List<Book> searchByTitle(String title) {
        ArrayList<Book> result = new ArrayList<>();
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(title.toLowerCase())) {
                result.add(book);
            }
        }
        return result;
    }

    public void addMember(Member member) {
        if (findMember(member.getId()) != null) {
            throw new IllegalArgumentException("Member ID already exists");
        }
        members.add(member);
    }

    public Member findMember(int id) {
        for (Member member : members) {
            if (member.getId() == id) return member;
        }
        return null;
    }

    public List<Member> getMembers() {
        return new ArrayList<>(members);
    }

    public List<BorrowRecord> getRecords() {
        return new ArrayList<>(records);
    }

    public BorrowRecord borrow(int bookId, int memberId) {
        Book book = findBook(bookId);

        if (book == null) {
            throw new IllegalArgumentException("Book not found");
        }
        if (findMember(memberId) == null) {
            throw new IllegalArgumentException("Member not found");
        }
        if (!book.borrowBook()) {
            throw new IllegalStateException("No copies available");
        }

        BorrowRecord record = new BorrowRecord(
            nextBorrowId++,
            bookId,
            memberId,
            LocalDate.now(),
            null
        );

        records.add(record);
        return record;
    }

    public void returnBorrow(int borrowId) {
        BorrowRecord found = null;

        for (BorrowRecord record : records) {
            if (record.getId() == borrowId) {
                found = record;
                break;
            }
        }

        if (found == null) {
            throw new IllegalArgumentException("Borrow record not found");
        }
        if (!found.isActive()) {
            throw new IllegalStateException("This loan was already returned");
        }

        Book book = findBook(found.getBookId());
        if (book == null || !book.returnBook()) {
            throw new IllegalStateException("Cannot return this book");
        }

        found.markReturned();
    }

    public String statistics() {
        int totalCopies = 0;
        int available = 0;
        int activeLoans = 0;

        for (Book book : books) {
            totalCopies += book.getTotalCopies();
            available += book.getAvailableCopies();
        }

        for (BorrowRecord record : records) {
            if (record.isActive()) activeLoans++;
        }

        return "Titles: " + books.size()
            + " | Total copies: " + totalCopies
            + " | Available copies: " + available
            + " | Members: " + members.size()
            + " | Active loans: " + activeLoans;
    }

    private static String encode(String value) {
        return Base64.getEncoder().encodeToString(
            value.getBytes(StandardCharsets.UTF_8)
        );
    }

    private static String decode(String value) {
        return new String(
            Base64.getDecoder().decode(value),
            StandardCharsets.UTF_8
        );
    }

    public void save() throws IOException {
        Files.createDirectories(dataFolder);

        ArrayList<String> bookLines = new ArrayList<>();
        for (Book b : books) {
            bookLines.add(
                b.getId() + "|"
                + encode(b.getTitle()) + "|"
                + encode(b.getAuthorName()) + "|"
                + b.getTotalCopies() + "|"
                + b.getAvailableCopies()
            );
        }

        ArrayList<String> memberLines = new ArrayList<>();
        for (Member m : members) {
            memberLines.add(
                m.getId() + "|" + encode(m.getName())
            );
        }

        ArrayList<String> recordLines = new ArrayList<>();
        for (BorrowRecord r : records) {
            recordLines.add(
                r.getId() + "|"
                + r.getBookId() + "|"
                + r.getMemberId() + "|"
                + r.getBorrowedOn() + "|"
                + (r.getReturnedOn() == null ? "" : r.getReturnedOn())
            );
        }

        Files.write(
            dataFolder.resolve("books.txt"),
            bookLines,
            StandardCharsets.UTF_8
        );

        Files.write(
            dataFolder.resolve("members.txt"),
            memberLines,
            StandardCharsets.UTF_8
        );

        Files.write(
            dataFolder.resolve("borrows.txt"),
            recordLines,
            StandardCharsets.UTF_8
        );
    }

    public void load() throws IOException {
        ArrayList<Book> loadedBooks = new ArrayList<>();
        ArrayList<Member> loadedMembers = new ArrayList<>();
        ArrayList<BorrowRecord> loadedRecords = new ArrayList<>();

        Path booksFile = dataFolder.resolve("books.txt");
        Path membersFile = dataFolder.resolve("members.txt");
        Path borrowsFile = dataFolder.resolve("borrows.txt");

        try {
            if (Files.exists(booksFile)) {
                for (String line : Files.readAllLines(
                        booksFile, StandardCharsets.UTF_8)) {

                    String[] p = line.split("\\|", -1);

                    if (p.length != 5) {
                        throw new IllegalArgumentException("Invalid books.txt row");
                    }

                    loadedBooks.add(new Book(
                        Integer.parseInt(p[0]),
                        decode(p[1]),
                        decode(p[2]),
                        Integer.parseInt(p[3]),
                        Integer.parseInt(p[4])
                    ));
                }
            }

            if (Files.exists(membersFile)) {
                for (String line : Files.readAllLines(
                        membersFile, StandardCharsets.UTF_8)) {

                    String[] p = line.split("\\|", -1);

                    if (p.length != 2) {
                        throw new IllegalArgumentException("Invalid members.txt row");
                    }

                    loadedMembers.add(new Member(
                        Integer.parseInt(p[0]),
                        decode(p[1])
                    ));
                }
            }

            if (Files.exists(borrowsFile)) {
                for (String line : Files.readAllLines(
                        borrowsFile, StandardCharsets.UTF_8)) {

                    String[] p = line.split("\\|", -1);

                    if (p.length != 5) {
                        throw new IllegalArgumentException("Invalid borrows.txt row");
                    }

                    loadedRecords.add(new BorrowRecord(
                        Integer.parseInt(p[0]),
                        Integer.parseInt(p[1]),
                        Integer.parseInt(p[2]),
                        LocalDate.parse(p[3]),
                        p[4].isEmpty() ? null : LocalDate.parse(p[4])
                    ));
                }
            }
        } catch (RuntimeException e) {
            throw new IOException(
                "Could not read saved data: " + e.getMessage(), e
            );
        }

        books.clear();
        books.addAll(loadedBooks);

        members.clear();
        members.addAll(loadedMembers);

        records.clear();
        records.addAll(loadedRecords);

        nextBorrowId = 1;
        for (BorrowRecord record : records) {
            nextBorrowId = Math.max(
                nextBorrowId,
                record.getId() + 1
            );
        }
    }
}
