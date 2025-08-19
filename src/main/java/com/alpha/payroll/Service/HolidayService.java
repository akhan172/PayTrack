package com.alpha.payroll.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alpha.payroll.DTO.HolidayDTO;
import com.alpha.payroll.Entity.Holiday;
import com.alpha.payroll.Repository.HolidayRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class HolidayService {
	@Autowired
	HolidayRepo holidayRepo;
	
	// Method to create a new holiday
	public HolidayDTO.Response createHoliday(HolidayDTO.Request holiday) {
		
		
		if(holidayRepo.existsByName(holiday.getName())) {
			log.error("Holiday with name {} already exists", holiday.getName());
			throw new RuntimeException("Holiday with this name already exists");
		}
		Holiday newHoliday = Holiday.builder()
				.name(holiday.getName())
				.date(holiday.getDate())
				.build();
		log.info("Creating new holiday: {}", newHoliday);
		Holiday savedHoliday = holidayRepo.save(newHoliday);
		log.info("Holiday created with ID: {}", savedHoliday.getId());
		return mapToResponseDTO(savedHoliday);
	}
	
	@Transactional(readOnly = true)
	public List<HolidayDTO.Response> getHolidaysBetweenDates(LocalDate startDate, LocalDate endDate) {
		log.info("Fetching holidays between {} and {}", startDate, endDate);
		
		List<Holiday> holidays = holidayRepo.findByDateBetween(startDate, endDate);
		
		return holidays.stream()
				.map(this::mapToResponseDTO)
				.toList();
	}
	
	@Transactional(readOnly = true)
	public boolean isHoliday(LocalDate date) {
		log.info("Checking if {} is a holiday", date);
		return holidayRepo.existsByDate(date);
	}
	
	
	private HolidayDTO.Response mapToResponseDTO(Holiday holiday) {
		return HolidayDTO.Response.builder()
				.id(holiday.getId())
				.name(holiday.getName())
				.date(holiday.getDate())
				.build();
	}

	public List<HolidayDTO.Response> getAllHolidays() {
        log.info("Fetching all holidays");
        
        List<Holiday> holidays = holidayRepo.findAll();
        return holidays.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
   
}