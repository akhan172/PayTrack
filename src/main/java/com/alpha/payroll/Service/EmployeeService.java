package com.alpha.payroll.Service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alpha.payroll.DTO.EmployeeDTO;
import com.alpha.payroll.Entity.Employee;
import com.alpha.payroll.Repository.EmployeeRepo;

import lombok.extern.slf4j.Slf4j;

//Create
//get all employee
//find employee by id
//get employee by id
@Service
@Slf4j
@Transactional
public class EmployeeService {
		// Method to create a new employee
	@Autowired
	EmployeeRepo employeeRepo;
	
	public EmployeeDTO.Response createEmployee(EmployeeDTO.Request employee) {
		// Implementation for creating an employee
		Employee newEmployee = Employee.builder()
				.Name(employee.getName())
				.email(employee.getEmail())
				.salary(employee.getSalary())
				.joiningDate(employee.getJoiningDate())
				.build();
		Employee savedEmployee = employeeRepo.save(newEmployee);
		log.info("Employee created with ID: {}", savedEmployee.getId());
		return mapToResponseDTO(savedEmployee);
	}

	// Method to get all employees
	@Transactional( readOnly = true)
	public List<EmployeeDTO.Response> getAllEmployees() {
		log.info("Fetching all employees");
		
		List<Employee> employees = employeeRepo.findAll();
		return employees.stream()
				.map(this::mapToResponseDTO)
				.toList();
	}

	// Method to find an employee by ID
	@Transactional(readOnly = true)
	public Employee findEmployeeById(Long id) {
		// Implementation for finding an employee by ID
		return employeeRepo.findById(id)
				.orElseThrow(() -> new RuntimeException("Employee not found with ID: " + id));
	}

	// Method to get an employee by ID
	public EmployeeDTO.Response getEmployeeById(Long id) {
		// Implementation for getting an employee by ID
		log.info("Fetching employee with ID: {}", id);
		Employee employee = findEmployeeById(id);
		
		return mapToResponseDTO(employee);
	}
	private EmployeeDTO.Response mapToResponseDTO(Employee employee) {
		return EmployeeDTO.Response.builder()
				.id(employee.getId())
				.name(employee.getName())
				.email(employee.getEmail())
				.joiningDate(employee.getJoiningDate())
				.salary(employee.getSalary())
				.build();
	}
}
