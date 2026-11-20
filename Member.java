public class Member {
    private final int id;
    private final String name;

    public Member(int id, String name) {
        if (id <= 0 || name == null || name.isBlank()) {
            throw new IllegalArgumentException("Invalid member details");
        }
        this.id = id;
        this.name = name.trim();
    }

    public int getId() { return id; }
    public String getName() { return name; }

    @Override
    public String toString() {
        return "Member ID: " + id + " | Name: " + name;
    }
}
