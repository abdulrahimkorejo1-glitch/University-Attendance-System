package universityattendance;

/** Student inherits common identity fields from Person. */
public class Student extends Person {
    private final String department;
    private final int semester;

    public Student(int id, String name, String department, int semester) {
        super(id, name);
        this.department = department;
        this.semester = semester;
    }

    public String getDepartment() { return department; }
    public int getSemester() { return semester; }

    @Override
    public String getPersonType() { return "Student"; }
}
