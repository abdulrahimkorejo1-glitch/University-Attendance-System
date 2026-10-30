# University Attendance Management System

A mini project built with **Java, OOP, Data Structures, File Handling and Swing GUI**.

## Features
- Add and view students
- Search students by ID or name
- Mark daily attendance as Present or Absent
- Prevent duplicate attendance for the same student on the same date
- View student attendance reports and percentage
- Save and load records using CSV files

## Technologies and concepts
- Java 17 or later
- Swing GUI
- OOP: abstraction, inheritance, encapsulation and method overriding
- Data structures: `ArrayList` and `HashMap`
- File handling: `BufferedReader`, `BufferedWriter`
- Date handling: `LocalDate`

## Project structure
```text
University-Attendance-System/
├── src/universityattendance/
│   ├── Person.java
│   ├── Student.java
│   ├── AttendanceRecord.java
│   ├── AttendanceManager.java
│   ├── FileStorage.java
│   └── UniversityAttendanceApp.java
├── data/
├── docs/
├── screenshots/
├── README.md
└── .gitignore
```

## Run the project

### Option 1: VS Code / terminal
Install JDK 17 or later. Open a terminal in the project folder and run:

**Windows**
```bat
javac -d out src\universityattendance\*.java
java -cp out universityattendance.UniversityAttendanceApp
```

**macOS / Linux**
```bash
javac -d out src/universityattendance/*.java
java -cp out universityattendance.UniversityAttendanceApp
```

### Option 2: NetBeans
Create a Java project, copy the `universityattendance` package into `Source Packages`, then run `UniversityAttendanceApp.java`.

## Data storage
The application creates `data/students.csv` and `data/attendance.csv` when records are saved. Keep the `data` folder with the project if you want to retain records.

## Notes
- This is an educational mini project, not a production attendance platform.
- The CSV format replaces commas in student names/departments with spaces.
- Add screenshots of the running application to the `screenshots` folder before final submission.
