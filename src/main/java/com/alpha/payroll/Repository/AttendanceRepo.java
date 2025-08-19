package com.alpha.payroll.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.alpha.payroll.AttendanceStatus;
import com.alpha.payroll.Entity.Attendance;

public interface AttendanceRepo extends JpaRepository<Attendance, Long> {

List<Attendance> findByEmployeeId(Long employeeId);
    
    List<Attendance> findByEmployeeIdAndDateBetween(Long employeeId, LocalDate startDate, LocalDate endDate);
    
    Optional<Attendance> findByEmployeeIdAndDate(Long employeeId, LocalDate date);
    
    boolean existsByEmployeeIdAndDate(Long employeeId, LocalDate date);
    
    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.employee.id = :employeeId " +
           "AND a.date BETWEEN :startDate AND :endDate AND a.status = :status")
    Long countByEmployeeAndDateRangeAndStatus(@Param("employeeId") Long employeeId,
                                            @Param("startDate") LocalDate startDate,
                                            @Param("endDate") LocalDate endDate,
                                            @Param("status") AttendanceStatus status);

	// Method to find attendance records by employee ID
	
}
