package com.alpha.payroll.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alpha.payroll.DTO.HolidayDTO;
import com.alpha.payroll.Service.HolidayService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/holidays")
@RequiredArgsConstructor
@Slf4j
public class HolidayController {
    @Autowired
    HolidayService holidayService;
    
    @PostMapping
    public ResponseEntity<HolidayDTO.Response> createHoliday(
            @Valid @RequestBody HolidayDTO.Request request) {
        
        log.info("Received request to create holiday: {} on {}", 
                request.getName(), request.getDate());
        
        HolidayDTO.Response response = holidayService.createHoliday(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @GetMapping
    public ResponseEntity<List<HolidayDTO.Response>> getAllHolidays() {
        log.info("Received request to fetch all holidays");
        
        List<HolidayDTO.Response> holidays = holidayService.getAllHolidays();
        return ResponseEntity.ok(holidays);
    }
}