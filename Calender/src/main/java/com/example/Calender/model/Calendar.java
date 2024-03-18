package com.example.Calender.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Calendar {

	private UUID id;
	private String ownerName;
	private List<Appointment> appointments;
	private List<TimeSlot> timeSlots;

	public boolean isAvailable(LocalDateTime dateTime, int duration, UUID timeSlotType) {
		// Check if the calendar is available at the given dateTime for the specified duration and timeSlotType
		// Implement logic here
		return true; // Placeholder implementation
	}

	public Calendar() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public List<Appointment> getAppointments() {
		return appointments;
	}

	public void setAppointments(List<Appointment> appointments) {
		this.appointments = appointments;
	}

	public List<TimeSlot> getTimeSlots() {
		return timeSlots;
	}

	public void setTimeSlots(List<TimeSlot> timeSlots) {
		this.timeSlots = timeSlots;
	}


}
