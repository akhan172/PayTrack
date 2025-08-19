package com.alpha.payroll.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alpha.payroll.Entity.Payroll;

public interface PayrollRepo extends JpaRepository<Payroll, Long> {

	List<Payroll> findByMonth(String monthStr);

	Payroll findByEmployeeIdAndMonth(Long employeeId, String monthStr);

	boolean existsByEmployeeIdAndMonth(Long id, String monthStr);

	// Custom query methods can be added here if needed
	// For example, to find payroll by employee and month:
	// Optional<Payroll> findByEmployeeAndMonth(Employee employee, String month);
	
	// You can also add methods for pagination, sorting, etc. as needed

}
