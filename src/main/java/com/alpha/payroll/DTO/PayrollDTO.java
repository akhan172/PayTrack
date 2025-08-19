package com.alpha.payroll.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class PayrollDTO {
	@Getter
	@Setter
	@EqualsAndHashCode
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class Response {
		private Long id;
		private Long employeeId;
		private String employeeName;
		private String employeeEmail;
		private String month;
		private Integer workingDays;
		private Integer paidOffDays;
		private Integer totalWorkingDays;
		private Double totalSalary;
	}
}
