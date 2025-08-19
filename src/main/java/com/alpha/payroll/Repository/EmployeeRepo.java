package com.alpha.payroll.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alpha.payroll.Entity.Employee;

public interface EmployeeRepo extends JpaRepository<Employee, Long> {
	
	// Custom query methods can be defined here if needed
	// For example, to find employees by name:
	// List<Employee> findByName(String name);
	
	// Or to find employees by email:
	// Optional<Employee> findByEmail(String email);

}
