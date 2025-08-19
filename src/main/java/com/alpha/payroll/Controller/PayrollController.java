package com.alpha.payroll.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alpha.payroll.DTO.PayrollDTO;
import com.alpha.payroll.Service.PayrollService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/payroll")
@RequiredArgsConstructor
@Slf4j
public class PayrollController {
    @Autowired
    PayrollService payrollService;
    
    @PostMapping("/generate")
    public ResponseEntity<List<PayrollDTO.Response>> generatePayrollForMonth(
            @RequestParam String month) {
        
        log.info("Received request to generate payroll for month: {}", month);
        
        List<PayrollDTO.Response> payrollResponses = payrollService.generatePayrollForMonth(month);
        return ResponseEntity.ok(payrollResponses);
    }
    
    @GetMapping("/{month}")
    public ResponseEntity<List<PayrollDTO.Response>> getPayrollForMonth( @PathVariable String month) {
       
        log.info("Received request to fetch payroll for month: {}", month);
        
        List<PayrollDTO.Response> payroll = payrollService.getPayrollForMonth(month);
        return ResponseEntity.ok(payroll);
    }
    
    @GetMapping("/employee/{employeeId}/month/{month}")
    public ResponseEntity<PayrollDTO.Response> getEmployeePayrollForMonth(@PathVariable Long employeeId, @PathVariable String month) {
        
        log.info("Received request to fetch payroll for employee {} for month: {}", 
                employeeId, month);
        
        PayrollDTO.Response payroll = payrollService.getEmployeePayrollForMonth(employeeId, month);
        return ResponseEntity.ok(payroll);
    }
}