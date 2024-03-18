package com.example.Calender.service.impl;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.Calender.model.Appointment;
import com.example.Calender.model.Calendar;
import com.example.Calender.model.TimeSlot;
import com.example.Calender.service.CalenderService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CalenderServiceImpl implements CalenderService{

	private static final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public List<LocalDateTime> findAvailableTime(List<UUID> calendarIds, Integer duration, String periodToSearch,
			UUID timeSlotType) {

		String[] periodParts = periodToSearch.split("/");
		LocalDateTime startTime = LocalDateTime.parse(periodParts[0], DateTimeFormatter.ISO_DATE_TIME);
		LocalDateTime endTime = LocalDateTime.parse(periodParts[1], DateTimeFormatter.ISO_DATE_TIME);

		List<Appointment> appointments = fetchAppointments(calendarIds,startTime,endTime);

		List<TimeSlot> timeSlots = fetchTimeSlots(calendarIds, startTime,endTime, timeSlotType);
		appointments.sort(Comparator.comparing(Appointment::getStart));
		timeSlots.sort(Comparator.comparing(TimeSlot::getStart));

		List<LocalDateTime> availableTimes = new ArrayList<>();

		// Start checking from the beginning of the period
		LocalDateTime currentTime = startTime;

		for (TimeSlot timeSlot : timeSlots) {
			// Ensure we're looking at future time slots and that there is enough gap for the duration
			while (!currentTime.isAfter(timeSlot.getEnd()) && !currentTime.plusMinutes(duration).isAfter(timeSlot.getEnd())) {
				boolean isAvailable = true;

				// Check against each appointment to see if the time slot is available
				for (Appointment appointment : appointments) {
					if ((currentTime.isBefore(appointment.getEnd()) && currentTime.plusMinutes(duration).isAfter(appointment.getStart())) ||
							(currentTime.equals(appointment.getStart()))) {
						isAvailable = false;
						break;
					}
				}

				// If available, add to the list and move to the next available slot after the duration
				if (isAvailable) {
					availableTimes.add(currentTime);
					// Assuming only one slot is needed per call. Remove the break if multiple slots are required.
					break;
				}

				// Move to the next time slot or duration, whichever is smaller, to check availability
				currentTime = currentTime.plusMinutes(Math.min(duration, java.time.Duration.between(currentTime, timeSlot.getEnd()).toMinutes()));
			}

			// Update the current time beyond the current time slot for the next iteration
			if (currentTime.isBefore(timeSlot.getEnd())) {
				currentTime = timeSlot.getEnd();
			}

			// Early exit if the end of the period is reached
			if (currentTime.isAfter(endTime)) {
				break;
			}
		}

		return availableTimes;
	}

	private List<Appointment> fetchAppointments(List<UUID> calendarIds, LocalDateTime startPeriod, LocalDateTime endPeriod) {

		try {
			File file = new File("src/main/resources/Danny boy.json.");
			return Arrays.asList(objectMapper.readValue(file, Appointment[].class));
		} catch (IOException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	private List<TimeSlot> fetchTimeSlots(List<UUID> calendarIds, LocalDateTime startPeriod,
			LocalDateTime endPeriod, UUID timeSlotType) {
		// Placeholder: Implement logic to fetch time slots based on calendarIds, period, and optionally timeSlotType
		List<TimeSlot> timeSlots = new ArrayList<>();

		try {
			File file = new File("src/main/resources/Danny boy.json.");
			timeSlots =  Arrays.asList(objectMapper.readValue(file, TimeSlot[].class));
		} catch (IOException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}

		return timeSlots;
	}

}
