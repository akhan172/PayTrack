package com.alpha.payroll.DTO;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class HolidayDTO {
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class Request {
		
		@NotBlank(message = "Holiday name cannot be blank")
		@Size(min = 1, max = 100, message = "Holiday name must be between 1 and 100 characters")
		private String name;
		
		@NotNull(message = "Holiday date cannot be null")
		private LocalDate date; // ISO format (yyyy-MM-dd)
	}
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class Response {
		
		private Long id;
		
		private String name;
		
		private LocalDate date; // ISO format (yyyy-MM-dd)
	}
}
