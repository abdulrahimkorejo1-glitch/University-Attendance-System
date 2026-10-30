package universityattendance;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/** Swing GUI for the University Attendance Management System. */
public class UniversityAttendanceApp extends JFrame {
    private final AttendanceManager manager = new AttendanceManager();
    private final FileStorage storage = new FileStorage();
    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"Student ID", "Name", "Department", "Semester", "Present", "Absent", "Attendance %"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };
    private final JTable table = new JTable(model);
    private final JTextField searchField = new JTextField(16);

    public UniversityAttendanceApp() {
        super("University Attendance Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 580);
        setLocationRelativeTo(null);
        buildUi();
        loadData();
        refreshTable("");
    }

    private void buildUi() {
        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        root.setBackground(new Color(245, 247, 250));

        JLabel title = new JLabel("University Attendance Management System");
        title.setFont(new Font("Arial", Font.BOLD, 23));
        title.setForeground(new Color(31, 55, 80));

        JPanel top = new JPanel(new BorderLayout(8, 8));
        top.setOpaque(false);
        top.add(title, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setOpaque(false);
        searchPanel.add(new JLabel("Search by ID or name:"));
        searchPanel.add(searchField);
        JButton searchButton = new JButton("Search");
        JButton showAllButton = new JButton("Show All");
        searchPanel.add(searchButton);
        searchPanel.add(showAllButton);
        top.add(searchPanel, BorderLayout.SOUTH);

        table.setRowHeight(26);
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        buttons.setOpaque(false);
        JButton addButton = new JButton("Add Student");
        JButton markButton = new JButton("Mark Attendance");
        JButton reportButton = new JButton("Student Report");
        JButton saveButton = new JButton("Save Data");
        buttons.add(addButton);
        buttons.add(markButton);
        buttons.add(reportButton);
        buttons.add(saveButton);

        searchButton.addActionListener(e -> refreshTable(searchField.getText().trim()));
        searchField.addActionListener(e -> refreshTable(searchField.getText().trim()));
        showAllButton.addActionListener(e -> { searchField.setText(""); refreshTable(""); });
        addButton.addActionListener(e -> addStudent());
        markButton.addActionListener(e -> markAttendance());
        reportButton.addActionListener(e -> showReport());
        saveButton.addActionListener(e -> saveData());

        root.add(top, BorderLayout.NORTH);
        root.add(new JScrollPane(table), BorderLayout.CENTER);
        root.add(buttons, BorderLayout.SOUTH);
        setContentPane(root);
    }

    private void addStudent() {
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField departmentField = new JTextField();
        JTextField semesterField = new JTextField();
        Object[] fields = {"Student ID:", idField, "Student Name:", nameField,
                "Department:", departmentField, "Semester:", semesterField};

        int result = JOptionPane.showConfirmDialog(this, fields, "Add New Student",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) return;

        try {
            int id = Integer.parseInt(idField.getText().trim());
            int semester = Integer.parseInt(semesterField.getText().trim());
            if (id <= 0 || semester <= 0 || nameField.getText().trim().isEmpty()
                    || departmentField.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("Please enter valid details in every field.");
            }
            manager.addStudent(new Student(id, nameField.getText().trim(),
                    departmentField.getText().trim(), semester));
            saveData();
            refreshTable("");
        } catch (NumberFormatException ex) {
            showError("Student ID and semester must be numbers.");
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        }
    }

    private void markAttendance() {
        List<Student> students = manager.getStudents();
        if (students.isEmpty()) { showError("Add a student first."); return; }

        String[] choices = students.stream()
                .map(s -> s.getId() + " - " + s.getName()).toArray(String[]::new);
        String selected = (String) JOptionPane.showInputDialog(this, "Select student:",
                "Mark Attendance", JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
        if (selected == null) return;

        int id = Integer.parseInt(selected.substring(0, selected.indexOf(" - ")));
        String[] statuses = {"Present", "Absent"};
        String status = (String) JOptionPane.showInputDialog(this, "Select status:",
                "Attendance for " + LocalDate.now(), JOptionPane.QUESTION_MESSAGE,
                null, statuses, statuses[0]);
        if (status == null) return;

        try {
            manager.addAttendance(id, LocalDate.now(), status);
            saveData();
            refreshTable("");
            JOptionPane.showMessageDialog(this, "Attendance saved successfully.");
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        }
    }

    private void showReport() {
        int row = table.getSelectedRow();
        if (row < 0) { showError("Select a student row first."); return; }
        int modelRow = table.convertRowIndexToModel(row);
        int id = Integer.parseInt(model.getValueAt(modelRow, 0).toString());
        Student student = manager.findStudent(id);
        StringBuilder report = new StringBuilder();
        report.append("Student: ").append(student.getName()).append("\n")
                .append("ID: ").append(id).append("\n")
                .append("Present: ").append(manager.getPresentCount(id)).append("\n")
                .append("Absent: ").append(manager.getAbsentCount(id)).append("\n")
                .append(String.format("Attendance: %.2f%%", manager.getPercentage(id)))
                .append("\n\nDate-wise records:\n");
        for (AttendanceRecord record : manager.getAttendanceFor(id)) {
            report.append(record.getDate()).append(" - ").append(record.getStatus()).append("\n");
        }
        JTextArea area = new JTextArea(report.toString(), 14, 38);
        area.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(area), "Student Attendance Report",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void refreshTable(String filter) {
        model.setRowCount(0);
        String query = filter.toLowerCase();
        for (Student student : manager.getStudents()) {
            if (!query.isEmpty()
                    && !String.valueOf(student.getId()).contains(query)
                    && !student.getName().toLowerCase().contains(query)) continue;
            model.addRow(new Object[]{student.getId(), student.getName(), student.getDepartment(),
                    student.getSemester(), manager.getPresentCount(student.getId()),
                    manager.getAbsentCount(student.getId()),
                    String.format("%.2f%%", manager.getPercentage(student.getId()))});
        }
    }

    private void loadData() {
        try { storage.load(manager); }
        catch (IOException ex) { showError("Could not load data: " + ex.getMessage()); }
    }

    private void saveData() {
        try { storage.save(manager); }
        catch (IOException ex) { showError("Could not save data: " + ex.getMessage()); }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Notice", JOptionPane.WARNING_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UniversityAttendanceApp().setVisible(true));
    }
}
