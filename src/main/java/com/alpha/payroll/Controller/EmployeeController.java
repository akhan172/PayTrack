package com.alpha.payroll.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alpha.payroll.DTO.EmployeeDTO;
import com.alpha.payroll.Service.EmployeeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
@Slf4j
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;
    
    @PostMapping
    public ResponseEntity<EmployeeDTO.Response> createEmployee(
            @Valid @RequestBody EmployeeDTO.Request request) {
        
        log.info("Received request to create employee: {}", request.getName());
        
        EmployeeDTO.Response response = employeeService.createEmployee(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @GetMapping
    public ResponseEntity<List<EmployeeDTO.Response>> getAllEmployees() {
        log.info("Received request to fetch all employees");
        
        List<EmployeeDTO.Response> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO.Response> getEmployeeById(@PathVariable Long id) {
        log.info("Received request to fetch employee with ID: {}", id);
        
        EmployeeDTO.Response employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }
}