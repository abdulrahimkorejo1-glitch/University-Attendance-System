# University Attendance Management System
## Mini Project Report

### 1. Introduction
The University Attendance Management System is a Java desktop application designed to maintain student information and daily attendance. It helps a user add students, mark attendance, search records and calculate attendance percentages.

### 2. Objectives
- Maintain a basic list of university students.
- Record each student's daily presence or absence.
- Search student records quickly.
- Calculate attendance totals and percentages.
- Save records so they remain available after closing the program.

### 3. Tools and Technologies
Java 17+, Java Swing, ArrayList, HashMap, CSV file handling and LocalDate.

### 4. Object-Oriented Programming
- **Abstraction:** `Person` is an abstract base class.
- **Inheritance:** `Student` extends `Person`.
- **Encapsulation:** Student and attendance fields are private.
- **Polymorphism:** `Student` overrides `getPersonType()`.

### 5. Data Structures
- **ArrayList:** Stores the collection of students and each student's attendance records.
- **HashMap:** Connects a student ID with that student's attendance list.

### 6. Main Modules
1. Add Student
2. Search Student
3. View Student List
4. Mark Attendance
5. Attendance Report
6. Save and Load Data

### 7. File Handling
The program saves student details in `data/students.csv` and attendance records in `data/attendance.csv`. These files are loaded when the application starts.

### 8. Conclusion
This mini project demonstrates how Java OOP concepts, data structures, GUI programming and file handling can be combined to solve a simple university attendance management problem.

### 9. Future Improvements
- Separate login accounts for teachers and administrators.
- Attendance by course and class section.
- Exportable PDF and Excel reports.
- Database connectivity using MySQL.
