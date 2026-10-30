package universityattendance;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Application logic. ArrayList and HashMap demonstrate data structures. */
public class AttendanceManager {
    private final ArrayList<Student> students = new ArrayList<>();
    private final HashMap<Integer, ArrayList<AttendanceRecord>> attendance = new HashMap<>();

    public List<Student> getStudents() {
        return new ArrayList<>(students);
    }

    public Student findStudent(int id) {
        for (Student student : students) {
            if (student.getId() == id) return student;
        }
        return null;
    }

    public void addStudent(Student student) {
        if (findStudent(student.getId()) != null) {
            throw new IllegalArgumentException("A student with this ID already exists.");
        }
        students.add(student);
        attendance.put(student.getId(), new ArrayList<>());
    }

    public void addAttendance(int studentId, LocalDate date, String status) {
        if (findStudent(studentId) == null) {
            throw new IllegalArgumentException("Student not found.");
        }
        if (!status.equals("Present") && !status.equals("Absent")) {
            throw new IllegalArgumentException("Status must be Present or Absent.");
        }
        ArrayList<AttendanceRecord> records = attendance.get(studentId);
        for (AttendanceRecord record : records) {
            if (record.getDate().equals(date)) {
                throw new IllegalArgumentException("Attendance has already been marked for this student today.");
            }
        }
        records.add(new AttendanceRecord(studentId, date, status));
    }

    public List<AttendanceRecord> getAttendanceFor(int studentId) {
        ArrayList<AttendanceRecord> records = attendance.get(studentId);
        return records == null ? new ArrayList<>() : new ArrayList<>(records);
    }

    public int getPresentCount(int studentId) {
        int count = 0;
        for (AttendanceRecord record : getAttendanceFor(studentId)) {
            if (record.getStatus().equals("Present")) count++;
        }
        return count;
    }

    public int getAbsentCount(int studentId) {
        int count = 0;
        for (AttendanceRecord record : getAttendanceFor(studentId)) {
            if (record.getStatus().equals("Absent")) count++;
        }
        return count;
    }

    public double getPercentage(int studentId) {
        int total = getPresentCount(studentId) + getAbsentCount(studentId);
        return total == 0 ? 0.0 : getPresentCount(studentId) * 100.0 / total;
    }

    void clearAll() {
        students.clear();
        attendance.clear();
    }

    void loadStudent(Student student) {
        students.add(student);
        attendance.put(student.getId(), new ArrayList<>());
    }

    void loadRecord(AttendanceRecord record) {
        if (attendance.containsKey(record.getStudentId())) {
            attendance.get(record.getStudentId()).add(record);
        }
    }
}
