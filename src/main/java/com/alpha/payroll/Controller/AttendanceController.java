package com.alpha.payroll.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alpha.payroll.DTO.AttendanceDTO;
import com.alpha.payroll.Service.AttendanceService;
import com.alpha.payroll.Service.EmployeeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/attendance")
@RequiredArgsConstructor
@Slf4j
public class AttendanceController {
    @Autowired
    AttendanceService attendanceService;
    @Autowired
    EmployeeService employeeService; // Assuming this is needed for employee details
    
    @PostMapping("/generate")
    public ResponseEntity<Map<String, String>> generateAttendanceForMonth(
            @RequestParam String month) {
        
        log.info("Received request to generate attendance for month: {}", month);
        
        attendanceService.generateAttendanceForMonth(month);
        
        return ResponseEntity.ok(Map.of(
                "message", "Attendance generated successfully for month: " + month,
                "month", month
        ));
    }
    
    @PostMapping("/mark")
    public ResponseEntity<AttendanceDTO.Response> markAttendance(
            @Valid @RequestBody AttendanceDTO.MarkRequest request) {
        
        log.info("Received request to mark attendance for employee {} on {}", 
                request.getEmployeeId(), request.getDate());
        
        AttendanceDTO.Response response = attendanceService.markAttendance(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @GetMapping("/{employeeId}")
    public ResponseEntity<List<AttendanceDTO.Response>> getEmployeeAttendance(
            @PathVariable Long employeeId) {
        
        log.info("Received request to fetch attendance for employee: {}", employeeId);
        
        List<AttendanceDTO.Response> attendance = attendanceService.getEmployeeAttendance(employeeId);
        return ResponseEntity.ok(attendance);
    }
    
    @GetMapping("/{employeeId}/month/{month}")
    public ResponseEntity<List<AttendanceDTO.Response>> getEmployeeAttendanceForMonth(
            @PathVariable Long employeeId,
            @PathVariable String month) {
        
        log.info("Received request to fetch attendance for employee {} for month: {}", 
                employeeId, month);
        
        List<AttendanceDTO.Response> attendance = attendanceService
                .getEmployeeAttendanceForMonth(employeeId, month);
        return ResponseEntity.ok(attendance);
    }
}