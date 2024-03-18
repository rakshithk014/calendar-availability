package com.example.Calender.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public interface CalenderService {

	/**
	 * Get Functionality for finding the availability time 
	 * @param calendarIds
	 * @param duration
	 * @param periodToSearch
	 * @param timeSlotType
	 * @return
	 */
    public List<LocalDateTime> findAvailableTime(List<UUID> calendarIds, Integer duration, String periodToSearch, UUID timeSlotType) ;

}
