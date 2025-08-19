package com.alpha.payroll.Repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alpha.payroll.Entity.Holiday;

public interface HolidayRepo extends JpaRepository<Holiday, Long> {

	boolean existsByName(String name);

	List<Holiday> findByDateBetween(LocalDate startDate, LocalDate endDate);

	boolean existsByDate(LocalDate date);

	// Custom query methods can be defined here if needed
	// For example, to find holidays by date range or type

}
