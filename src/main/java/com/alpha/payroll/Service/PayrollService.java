package com.alpha.payroll.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alpha.payroll.AttendanceStatus;
import com.alpha.payroll.DTO.PayrollDTO;
import com.alpha.payroll.Entity.Employee;
import com.alpha.payroll.Entity.Payroll;
import com.alpha.payroll.Repository.AttendanceRepo;
import com.alpha.payroll.Repository.PayrollRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PayrollService {
	@Autowired
	PayrollRepo payrollRepository;
	@Autowired
	AttendanceRepo attendanceRepository;
	@Autowired
	EmployeeService employeeService;

	public List<PayrollDTO.Response> generatePayrollForMonth(String monthStr) {
		log.info("Generating payroll for month: {}", monthStr);

		YearMonth yearMonth = YearMonth.parse(monthStr);
		LocalDate startDate = yearMonth.atDay(1);
		LocalDate endDate = yearMonth.atEndOfMonth();

		List<Employee> employees = employeeService.getAllEmployees().stream()
				.map(dto -> employeeService.findEmployeeById(dto.getId())).collect(Collectors.toList());

		List<PayrollDTO.Response> payrollResponses = employees.stream()
				.map(employee -> generatePayrollForEmployee(employee, monthStr, startDate, endDate))
				.collect(Collectors.toList());

		log.info("Generated payroll for {} employees for month: {}", payrollResponses.size(), monthStr);
		return payrollResponses;
	}

	@Transactional(readOnly = true)
	public List<PayrollDTO.Response> getPayrollForMonth(String monthStr) {
		log.info("Fetching payroll records for month: {}", monthStr);

		List<Payroll> payrolls = payrollRepository.findByMonth(monthStr);

		if (payrolls.isEmpty()) {
			throw new RuntimeException(
					"No payroll records found for month: " + monthStr + ". Please generate payroll first.");
		}

		return payrolls.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public PayrollDTO.Response getEmployeePayrollForMonth(Long employeeId, String monthStr) {
		log.info("Fetching payroll for employee: {} for month: {}", employeeId, monthStr);

		Employee employee = employeeService.findEmployeeById(employeeId);
		Payroll payroll = payrollRepository.findByEmployeeIdAndMonth(employee.getId(), monthStr);

		return mapToResponseDTO(payroll);
	}

	private PayrollDTO.Response generatePayrollForEmployee(Employee employee, String monthStr, LocalDate startDate,
			LocalDate endDate) {

		// Check if payroll already exists
		Payroll payroll = payrollRepository.findByEmployeeIdAndMonth(employee.getId(), monthStr);

		if (payroll != null) {
			log.info("Payroll already exists for employee {} for month {}. Updating...", employee.getId(), monthStr);
		} else {
			log.info("No payroll found for employee {} for month {}. Creating new...", employee.getId(), monthStr);
			payroll = Payroll.builder().employee(employee).month(monthStr).build();
		}

		// Count present days
		Long presentDays = attendanceRepository.countByEmployeeAndDateRangeAndStatus(employee.getId(), startDate,
				endDate, AttendanceStatus.PRESENT);

		// Count paid off days (Sundays + Holidays)
		Long sundayDays = attendanceRepository.countByEmployeeAndDateRangeAndStatus(employee.getId(), startDate,
				endDate, AttendanceStatus.SUNDAY);

		Long holidayDays = attendanceRepository.countByEmployeeAndDateRangeAndStatus(employee.getId(), startDate,
				endDate, AttendanceStatus.HOLIDAY);

		Integer totalPresentDays = presentDays.intValue();
		Integer totalPaidOffDays = (int) (sundayDays + holidayDays);

		// Calculate total salary
		Double totalSalary;
		if (totalPresentDays == 0) {
			totalSalary = 0.0;
			totalPaidOffDays = 0; // No salary if no present days
		} else {
			totalSalary = (totalPresentDays + totalPaidOffDays) * employee.getSalary();
		}

		// Set values
		payroll.setWorkingDays(totalPresentDays);
		payroll.setPaidOffDays(totalPaidOffDays);
		payroll.setTotalSalary(totalSalary);

		// Save payroll
		Payroll savedPayroll = payrollRepository.save(payroll);

		log.info("Generated payroll for employee {}: Present={}, PaidOff={}, Salary={}", employee.getId(),
				totalPresentDays, totalPaidOffDays, totalSalary);

		return mapToResponseDTO(savedPayroll);
	}

	private PayrollDTO.Response mapToResponseDTO(Payroll payroll) {
		Employee employee = payroll.getEmployee();

		return PayrollDTO.Response.builder().id(payroll.getId()).employeeId(employee.getId())
				.employeeName(employee.getName()).month(payroll.getMonth()).workingDays(payroll.getWorkingDays())
				.employeeEmail(employee.getEmail())
				.paidOffDays(payroll.getPaidOffDays())
				.totalWorkingDays(payroll.getWorkingDays() + payroll.getPaidOffDays())
				.totalSalary(payroll.getTotalSalary()).build();
	}
}
