package com.alpha.payroll.DTO;

import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import lombok.*;



public class EmployeeDTO {
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	@Getter
	@Setter	
	public static class Request {
		
		@NotBlank(message = "Name is required")
		private String name;
		
		@NotBlank(message = "Email is required")
		private String email;
		
		@NotNull(message = "Joining date is required")
		@PastOrPresent(message = "Joining date must be in the past or present")
		private LocalDate joiningDate;
		
		@NotNull(message = "Salary is required")
		@DecimalMin(value = "0.0", inclusive = false, message = "Salary must be greater than 0")
		private Double salary;

		
	}
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class Response {
		
		private Long id;
		
		private String name;
		
		private String email;
		
		private LocalDate joiningDate;
		
		private Double salary;
		
		
	}
}
