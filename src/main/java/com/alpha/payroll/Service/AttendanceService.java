package com.alpha.payroll.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alpha.payroll.AttendanceStatus;
import com.alpha.payroll.DTO.AttendanceDTO;
import com.alpha.payroll.Entity.Attendance;
import com.alpha.payroll.Entity.Employee;
import com.alpha.payroll.Repository.AttendanceRepo;
import com.alpha.payroll.Repository.EmployeeRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AttendanceService {
    @Autowired
     AttendanceRepo attendanceRepository;
    @Autowired
     EmployeeService employeeService;
    @Autowired
     EmployeeRepo employeeRepository;
    @Autowired// Added direct repository access
     HolidayService holidayService;
    
    public void generateAttendanceForMonth(String monthStr) {
        log.info("Generating attendance for month: {}", monthStr);
        
        YearMonth yearMonth = YearMonth.parse(monthStr);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        
        // Optimized: Single query to fetch all employees
        List<Employee> employees = employeeRepository.findAll();
        
        int totalRecordsGenerated = 0;
        
        for (Employee employee : employees) {
            LocalDate currentDate = startDate;
            //int cnt=0;
            //List<Attendance> attenList = new ArrayList<>();
            
            while (!currentDate.isAfter(endDate)) {
                if (!attendanceRepository.existsByEmployeeIdAndDate(employee.getId(), currentDate)) {
                    AttendanceStatus status = determineAttendanceStatus(currentDate);
                    
                    if (status != null) {
                    	if(status == AttendanceStatus.PRESENT)cnt++;
                        Attendance attendance = Attendance.builder()
                                .employee(employee)
                                .date(currentDate)
                                .status(status)
                                .build();
                        
                        attendanceRepository.save(attendance);
                        totalRecordsGenerated++;
                    }
                }
                currentDate = currentDate.plusDays(1);
            }
        }
        
        log.info("Generated {} attendance records for month: {}", totalRecordsGenerated, monthStr);
    }
    
    public AttendanceDTO.Response markAttendance(AttendanceDTO.MarkRequest request) {
        log.info("Marking attendance for employee {} on {}", request.getEmployeeId(), request.getDate());
        
        Employee employee = employeeService.findEmployeeById(request.getEmployeeId());
        
        // Validate that the date is not in the future
        if (request.getDate().isAfter(LocalDate.now())) {
            throw new RuntimeException("Cannot mark attendance for future dates");
        }
        
        // Validate status for weekends and holidays
        validateAttendanceStatus(request.getDate(), request.getStatus());
        
        Optional<Attendance> existingAttendance = attendanceRepository
                .findByEmployeeIdAndDate(request.getEmployeeId(), request.getDate());
        
        Attendance attendance;
        if (existingAttendance.isPresent()) {
            attendance = existingAttendance.get();
            AttendanceStatus oldStatus = attendance.getStatus();
            attendance.setStatus(request.getStatus());
            log.info("Updated attendance status from {} to {} for employee {} on {}", 
                    oldStatus, request.getStatus(), request.getEmployeeId(), request.getDate());
        } else {
            attendance = Attendance.builder()
                    .employee(employee)
                    .date(request.getDate())
                    .status(request.getStatus())
                    .build();
            log.info("Created new attendance record for employee {} on {}", 
                    request.getEmployeeId(), request.getDate());
        }
        
        Attendance savedAttendance = attendanceRepository.save(attendance);
        return mapToResponseDTO(savedAttendance);
    }
    
    @Transactional(readOnly = true)
    public List<AttendanceDTO.Response> getEmployeeAttendance(Long employeeId) {
        log.info("Fetching attendance records for employee: {}", employeeId);
        
        Employee employee = employeeService.findEmployeeById(employeeId);
        List<Attendance> attendanceRecords = attendanceRepository.findByEmployeeId(employeeId);
        
        return attendanceRecords.stream()
        		.filter(record->record.getStatus()==AttendanceStatus.PRESENT)
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<AttendanceDTO.Response> getEmployeeAttendanceForMonth(Long employeeId, String monthStr) {
        log.info("Fetching attendance records for employee: {} for month: {}", employeeId, monthStr);
        
        Employee employee = employeeService.findEmployeeById(employeeId);
        YearMonth yearMonth = YearMonth.parse(monthStr);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        
        List<Attendance> attendanceRecords = attendanceRepository
                .findByEmployeeIdAndDateBetween(employeeId, startDate, endDate);
        
        return attendanceRecords.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    
    private AttendanceStatus determineAttendanceStatus(LocalDate date) {
        if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            return AttendanceStatus.SUNDAY;
        }
        
        if (holidayService.isHoliday(date)) {
            return AttendanceStatus.HOLIDAY;
        }
        
        // For weekdays (Monday to Friday) and Saturday, return null 
        // so they can be marked manually
        return null;
    }
    
    private void validateAttendanceStatus(LocalDate date, AttendanceStatus status) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        boolean isHoliday = holidayService.isHoliday(date);
        
        // Sunday should only be marked as SUNDAY
        if (dayOfWeek == DayOfWeek.SUNDAY && status != AttendanceStatus.SUNDAY) {
            throw new RuntimeException("Sunday can only be marked as SUNDAY status");
        }
        
        // Holiday should only be marked as HOLIDAY
        if (isHoliday && status != AttendanceStatus.HOLIDAY) {
            throw new RuntimeException("Holiday dates can only be marked as HOLIDAY status");
        }
        
        // Cannot mark SUNDAY status on non-Sunday
        if (status == AttendanceStatus.SUNDAY && dayOfWeek != DayOfWeek.SUNDAY) {
            throw new RuntimeException("SUNDAY status can only be used for Sundays");
        }
        
        // Cannot mark HOLIDAY status on non-holiday
        if (status == AttendanceStatus.HOLIDAY && !isHoliday) {
            throw new RuntimeException("HOLIDAY status can only be used for declared holidays");
        }
    }
    
    private AttendanceDTO.Response mapToResponseDTO(Attendance attendance) {
        return AttendanceDTO.Response.builder()
                .id(attendance.getId())
                .employeeId(attendance.getEmployee().getId())
                .employeeName(attendance.getEmployee().getName())
                .date(attendance.getDate())
                .status(attendance.getStatus())
                .build();
    }
}