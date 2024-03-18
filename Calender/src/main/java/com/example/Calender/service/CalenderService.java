package com.example.Calender.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public interface CalenderService {

    public List<LocalDateTime> findAvailableTime(List<UUID> calendarIds, Integer duration, String periodToSearch, UUID timeSlotType) ;

}
