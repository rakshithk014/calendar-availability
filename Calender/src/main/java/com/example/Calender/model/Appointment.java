package com.example.Calender.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Appointment {

	private UUID id;
	private UUID calendarId;
	private LocalDateTime start;
	private LocalDateTime end;
	public Appointment() {
		super();
		// TODO Auto-generated constructor stub
	}
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public UUID getCalendarId() {
		return calendarId;
	}
	public void setCalendarId(UUID calendarId) {
		this.calendarId = calendarId;
	}
	public LocalDateTime getStart() {
		return start;
	}
	public void setStart(LocalDateTime start) {
		this.start = start;
	}
	public LocalDateTime getEnd() {
		return end;
	}
	public void setEnd(LocalDateTime end) {
		this.end = end;
	}

}
