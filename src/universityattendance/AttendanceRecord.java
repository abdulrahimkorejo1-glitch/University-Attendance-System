package universityattendance;

import java.time.LocalDate;

public class AttendanceRecord {
    private final int studentId;
    private final LocalDate date;
    private final String status; // Present or Absent

    public AttendanceRecord(int studentId, LocalDate date, String status) {
        this.studentId = studentId;
        this.date = date;
        this.status = status;
    }

    public int getStudentId() { return studentId; }
    public LocalDate getDate() { return date; }
    public String getStatus() { return status; }
}
