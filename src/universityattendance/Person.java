package universityattendance;

/** Base class demonstrating abstraction. */
public abstract class Person {
    private final int id;
    private final String name;

    protected Person(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }

    public abstract String getPersonType();
}
