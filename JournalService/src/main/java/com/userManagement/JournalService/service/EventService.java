package com.userManagement.JournalService.service;

import com.userManagement.JournalService.entity.Event;
import com.userManagement.JournalService.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService {
    @Autowired
    private  EventRepository eventRepository;


    @KafkaListener(topics = "user-events", groupId = "journal-group")
    public void consumeEvent(String message) {
        Event event = new Event();
        event.setEventMessage(message);
        event.setTimestamp(LocalDateTime.now());
        eventRepository.save(event);
        eventRepository.save(event);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }
}
