package universityattendance;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

/** Reads and writes project data as simple CSV files. */
public class FileStorage {
    private final Path dataDirectory = Path.of("data");
    private final Path studentsFile = dataDirectory.resolve("students.csv");
    private final Path attendanceFile = dataDirectory.resolve("attendance.csv");

    public void save(AttendanceManager manager) throws IOException {
        Files.createDirectories(dataDirectory);

        try (BufferedWriter writer = Files.newBufferedWriter(studentsFile, StandardCharsets.UTF_8)) {
            writer.write("id,name,department,semester");
            writer.newLine();
            for (Student student : manager.getStudents()) {
                writer.write(student.getId() + "," + clean(student.getName()) + ","
                        + clean(student.getDepartment()) + "," + student.getSemester());
                writer.newLine();
            }
        }

        try (BufferedWriter writer = Files.newBufferedWriter(attendanceFile, StandardCharsets.UTF_8)) {
            writer.write("studentId,date,status");
            writer.newLine();
            for (Student student : manager.getStudents()) {
                for (AttendanceRecord record : manager.getAttendanceFor(student.getId())) {
                    writer.write(record.getStudentId() + "," + record.getDate() + "," + record.getStatus());
                    writer.newLine();
                }
            }
        }
    }

    public void load(AttendanceManager manager) throws IOException {
        manager.clearAll();
        Files.createDirectories(dataDirectory);

        if (Files.exists(studentsFile)) {
            try (BufferedReader reader = Files.newBufferedReader(studentsFile, StandardCharsets.UTF_8)) {
                String line;
                boolean header = true;
                while ((line = reader.readLine()) != null) {
                    if (header) { header = false; continue; }
                    String[] p = line.split(",", -1);
                    if (p.length != 4) continue;
                    try {
                        manager.loadStudent(new Student(Integer.parseInt(p[0]), p[1], p[2],
                                Integer.parseInt(p[3])));
                    } catch (NumberFormatException ignored) { }
                }
            }
        }

        if (Files.exists(attendanceFile)) {
            try (BufferedReader reader = Files.newBufferedReader(attendanceFile, StandardCharsets.UTF_8)) {
                String line;
                boolean header = true;
                while ((line = reader.readLine()) != null) {
                    if (header) { header = false; continue; }
                    String[] p = line.split(",", -1);
                    if (p.length != 3) continue;
                    try {
                        manager.loadRecord(new AttendanceRecord(Integer.parseInt(p[0]),
                                LocalDate.parse(p[1]), p[2]));
                    } catch (RuntimeException ignored) { }
                }
            }
        }
    }

    private String clean(String value) {
        return value.replace(",", " ").replace("\n", " ").replace("\r", " ");
    }
}
