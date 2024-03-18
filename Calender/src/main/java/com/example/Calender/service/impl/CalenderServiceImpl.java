package com.example.Calender.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
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
import com.fasterxml.jackson.databind.JsonNode;
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

		List<Appointment> appointments = new ArrayList<>();
		File file = new File("src/main/resources/jsonFiles/Danny boy.json");
		
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			JsonNode jsonNode = objectMapper.readTree(file);
			JsonNode appointmentsNode = jsonNode.get("appointments");
			if (appointmentsNode.isArray()) {
			    for (JsonNode appointment : appointmentsNode) {
					Appointment appt= new Appointment(); 
					appt.setId(UUID.fromString(appointment.get("id").asText()));
			        appt.setCalendarId(UUID.fromString(appointment.get("calendar_id").asText()));
			        appt.setStart(LocalDateTime.parse(appointment.get("start").asText(), DateTimeFormatter.ISO_DATE_TIME));
			        appt.setEnd(LocalDateTime.parse(appointment.get("end").asText(), DateTimeFormatter.ISO_DATE_TIME));
			        appointments.add(appt);
			    }
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return appointments;
	}

	private List<TimeSlot> fetchTimeSlots(List<UUID> calendarIds, LocalDateTime startPeriod,
			LocalDateTime endPeriod, UUID timeSlotType) {
		List<TimeSlot> timeSlots = new ArrayList<>();

		File file = new File("src/main/resources/jsonFiles/Danny boy.json");
		
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			JsonNode jsonNode = objectMapper.readTree(file);
			JsonNode timeSlotNode = jsonNode.get("timeslots");
			if (timeSlotNode.isArray()) {
			    for (JsonNode slot : timeSlotNode) {
			    	TimeSlot timeSlot= new TimeSlot(); 
			    	timeSlot.setId(UUID.fromString(slot.get("id").asText()));
					timeSlot.setCalendarId(UUID.fromString(slot.get("calendar_id").asText()));
					timeSlot.setStart(LocalDateTime.parse(slot.get("start").asText(), DateTimeFormatter.ISO_DATE_TIME));
					timeSlot.setEnd(LocalDateTime.parse(slot.get("end").asText(), DateTimeFormatter.ISO_DATE_TIME));
					timeSlots.add(timeSlot);
			    }
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return timeSlots;
	}

}
