# PayTrack (Employee Attendance & Payroll System)

A comprehensive Spring Boot application for managing employee attendance and payroll calculations with automated features for holiday management and salary computation.

## 🚀 Features

- **Employee Management**: Add and manage employee records with designation and daily wage
- **Holiday Management**: Define holidays that are automatically considered as paid days
- **Automated Attendance Generation**: Auto-generate attendance records for entire months
- **Smart Status Assignment**: Automatically marks Sundays and holidays appropriately
- **Manual Attendance Marking**: Mark specific days as Present/Absent through REST APIs
- **Payroll Automation**: Calculate monthly salaries based on attendance records
- **Saturday Logic**: Saturdays are paid only if employee is present

## 🛠 Technology Stack

- **Java 17**
- **Spring Boot 3.2.2**
- **Spring Data JPA** (Hibernate)
- **MySQL Database**
- **Lombok** for boilerplate code reduction
- **Bean Validation** for input validation
- **Maven** for dependency management

## 📋 Prerequisites

- Java 17 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher

## ⚙️ Setup Instructions

### 1. Clone the Repository
```bash
git clone <repository-url>
cd employee-attendance-payroll
```

### 2. Database Setup
```sql
-- Create MySQL database
CREATE DATABASE attendance_payroll_db;

-- Create a user (optional)
CREATE USER 'attendance_user'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON attendance_payroll_db.* TO 'attendance_user'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Configure Database
Update `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/attendance_payroll_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=your_password
```

### 4. Run the Application
```bash
mvn clean install
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## 📚 API Documentation

### Employee APIs

#### Create Employee
```http
POST /employees
Content-Type: application/json

{
  "name": "John Doe",
  "designation": "Software Engineer",
  "joinDate": "2024-01-15",
  "dailyWage": 500.00
}
```

#### Get All Employees
```http
GET /employees
```

#### Get Employee by ID
```http
GET /employees/{id}
```

### Holiday APIs

#### Create Holiday
```http
POST /holidays
Content-Type: application/json

{
  "holidayDate": "2024-01-26",
  "holidayName": "Republic Day"
}
```

#### Get All Holidays
```http
GET /holidays
```

### Attendance APIs

#### Generate Attendance for Month
```http
POST /attendance/generate?month=2024-03
```
This automatically creates attendance records for all employees for the specified month:
- Sundays → marked as "SUNDAY"
- Declared holidays → marked as "HOLIDAY"  
- Other days → left unmarked for manual entry

#### Mark Attendance
```http
POST /attendance/mark
Content-Type: application/json

{
  "employeeId": 1,
  "date": "2024-03-15",
  "status": "PRESENT"
}
```
Status options: `PRESENT`, `ABSENT`, `HOLIDAY`, `SUNDAY`

#### Get Employee Attendance
```http
GET /attendance/{employeeId}
```

#### Get Employee Attendance for Month
```http
GET /attendance/{employeeId}/month/{month}
```

### Payroll APIs

#### Generate Payroll for Month
```http
POST /payroll/generate?month=2024-03
```
This calculates and saves payroll for all employees based on:
- Present days × daily wage
- Paid off days (Sundays + Holidays) × daily wage
- Saturday is paid only if marked as present

#### Get Payroll for Month
```http
GET /payroll/{month}
```

#### Get Employee Payroll for Month
```http
GET /payroll/employee/{employeeId}/month/{month}
```

## 📖 Business Logic

### Attendance Rules
1. **Sundays**: Automatically marked as "SUNDAY" - counted as paid days
2. **Holidays**: Automatically marked as "HOLIDAY" - counted as paid days  
3. **Saturdays**: Must be manually marked - paid only if present
4. **Weekdays**: Must be manually marked as Present/Absent

### Payroll Calculation
```
Total Salary = (Present Days + Paid Off Days) × Daily Wage

Where:
- Present Days = Days marked as "PRESENT"
- Paid Off Days = Days marked as "SUNDAY" + Days marked as "HOLIDAY"
```

### Validation Rules
- Cannot mark attendance for future dates
- Sunday can only be marked as "SUNDAY"
- Holiday dates can only be marked as "HOLIDAY"
- Cannot use "SUNDAY" status for non-Sunday dates
- Cannot use "HOLIDAY" status for non-holiday dates

## 🗂 Project Structure

```
src/main/java/com/example/attendance/
├── EmployeeAttendancePayrollApplication.java
├── controller/
│   ├── EmployeeController.java
│   ├── HolidayController.java
│   ├── AttendanceController.java
│   └── PayrollController.java
├── service/
│   ├── EmployeeService.java
│   ├── HolidayService.java
│   ├── AttendanceService.java
│   └── PayrollService.java
├── repository/
│   ├── EmployeeRepository.java
│   ├── HolidayRepository.java
│   ├── AttendanceRepository.java
│   └── PayrollRepository.java
├── entity/
│   ├── Employee.java
│   ├── Holiday.java
│   ├── Attendance.java
│   └── Payroll.java
├── dto/
│   ├── EmployeeDTO.java
│   ├── HolidayDTO.java
│   ├── AttendanceDTO.java
│   └── PayrollDTO.java
├── enums/
│   └── AttendanceStatus.java
└── exception/
    ├── ResourceNotFoundException.java
    ├── BusinessException.java
    ├── DuplicateResourceException.java
    └── GlobalExceptionHandler.java
```

## 🧪 Sample Usage Workflow

1. **Add Employees**:
  

2. **Add Holidays**:
  

3. **Generate Attendance for March 2024**:
  

4. **Mark Some Days as Present**:
   

5. **Generate Payroll**:
  

6. **View Payroll**:
  

## 🚨 Error Handling

The application includes comprehensive error handling:
- **404**: Resource not found (Employee, Holiday, etc.)
- **400**: Validation errors, business logic violations
- **409**: Duplicate resources (holiday on same date)
- **500**: Internal server errors

All errors return structured JSON responses with timestamps and details.

## 📝 Logging

The application uses SLF4J with Logback for logging:
- **INFO**: Business operations, API calls
- **DEBUG**: Detailed execution flow
- **ERROR**: Exception details


## 📄 License

This project is licensed under the MIT License.
