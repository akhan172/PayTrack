package com.alpha.payroll.DTO;

import java.time.LocalDate;

import com.alpha.payroll.AttendanceStatus;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class AttendanceDTO {
	
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	@Getter
	@Setter
	public static class MarkRequest {
		@NotNull(message = "Employee ID cannot be blank")
		private Long employeeId;
		@NotNull(message = "Attendance status cannot be blank")
		private LocalDate date;
		@NotNull(message = "Status cannot be blank")
		private AttendanceStatus status;
		
	}
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	@Getter
	@Setter
	public static class Response {
		private Long id;
		private Long employeeId;
		private String employeeName;
		private LocalDate date;
		private AttendanceStatus status;
	}
}
