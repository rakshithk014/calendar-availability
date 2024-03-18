package com.example.Calender.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Calender.service.CalenderService;

@RestController
public class MeetingController {

	@Autowired
	private CalenderService calendarService;

	@GetMapping("/availabletime")
	public List<LocalDateTime> getAvailableTime(@RequestParam List<UUID> calendarIds, @RequestParam Integer duration, @RequestParam String periodToSearch, @RequestParam(required = false) UUID timeSlotType) {
		return calendarService.findAvailableTime(calendarIds, duration, periodToSearch, timeSlotType);
	}
}
