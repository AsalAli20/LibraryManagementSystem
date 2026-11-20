import java.time.LocalDate;

public class BorrowRecord {
    private final int id;
    private final int bookId;
    private final int memberId;
    private final LocalDate borrowedOn;
    private LocalDate returnedOn;

    public BorrowRecord(int id, int bookId, int memberId, LocalDate borrowedOn, LocalDate returnedOn) {
        if (id <= 0 || bookId <= 0 || memberId <= 0 || borrowedOn == null
                || (returnedOn != null && returnedOn.isBefore(borrowedOn))) {
            throw new IllegalArgumentException("Invalid borrow record");
        }
        this.id = id;
        this.bookId = bookId;
        this.memberId = memberId;
        this.borrowedOn = borrowedOn;
        this.returnedOn = returnedOn;
    }

    public int getId() { return id; }
    public int getBookId() { return bookId; }
    public int getMemberId() { return memberId; }
    public LocalDate getBorrowedOn() { return borrowedOn; }
    public LocalDate getReturnedOn() { return returnedOn; }
    public boolean isActive() { return returnedOn == null; }

    public void markReturned() {
        if (!isActive()) throw new IllegalStateException("Already returned");
        returnedOn = LocalDate.now();
    }

    @Override
    public String toString() {
        return "Borrow ID: " + id + " | Book ID: " + bookId + " | Member ID: " + memberId
                + " | Borrowed: " + borrowedOn + " | Returned: "
                + (returnedOn == null ? "Not yet" : returnedOn);
    }
}
