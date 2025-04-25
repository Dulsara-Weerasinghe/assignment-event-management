package com.event.service.service.impl;

import com.event.service.domain.Event;
import com.event.service.dto.CreateEventRequest;
import com.event.service.dto.FilterEvents;
import com.event.service.dto.UpcomingEventsRequest;
import com.event.service.dto.UpdateEvent;
import com.event.service.enums.EventVisibilityType;
import com.event.service.exception.ApplicationException;
import com.event.service.exception.DataNotFounException;
import com.event.service.repository.EventRepository;
import com.event.service.service.EventService;
import com.event.service.util.ErrorDsc;
import com.event.service.util.EventUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public String createEvent(CreateEventRequest createEventRequest) {
        try {
            Event event = new Event();
            event.setId(EventUtil.generateEventId());
            event.setTitle(createEventRequest.getTitle());
            event.setDescription(createEventRequest.getDescription());
            event.setLocation(createEventRequest.getLocation());
            event.setStartTime(createEventRequest.getStartTime());
            event.setEndTime(createEventRequest.getEndTime());
            event.setVisibility(EventVisibilityType.valueOf(createEventRequest.getEventVisibilityType().name()));
            event.setCreatedAt(LocalDateTime.now());
            Event eventCreate = eventRepository.save(event);
            if (eventCreate != null) {
                return event.getId();
            } else {
                return null;
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ApplicationException("100001", "error.create.event");
        }
    }

    @Override
    public ResponseEntity<?> updateTask(UpdateEvent updateEvent, String eventId) throws DataNotFounException {
        log.debug("Event update request" + updateEvent);

        Event task = eventRepository.findById(eventId).orElseThrow(() -> new DataNotFounException(ErrorDsc.ERR_DSC_TASK_NOT_FOUND));
        task.setDescription(updateEvent.getDescription());
        task.setVisibility(EventVisibilityType.valueOf(updateEvent.getVisibility()));
        task.setLocation(updateEvent.getLocation());
        task.setTitle(updateEvent.getTitle());
        task.setHostId(updateEvent.getHostId());
        Event save = eventRepository.save(task);


        return new ResponseEntity<>(save, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> deleteEvent(String eventId) {
        eventRepository.deleteById(eventId);

        return new ResponseEntity<>(eventId, HttpStatus.OK);
    }

    @Override
    public List<Event> filterEvents(FilterEvents filterEvents) {

        List<Event> eventList = eventRepository.getList(filterEvents.getDate(), filterEvents.getLocation(), filterEvents.getVisibility());
        if (!eventList.isEmpty()) {
            return eventList;
        } else {
            return new ArrayList<>();
        }

    }

    @Override
    public List<Event> upcomingEvents(UpcomingEventsRequest upcomingEventsRequest) {
        Pageable pageable = PageRequest.of(upcomingEventsRequest.getPage(), upcomingEventsRequest.getSize(), Sort.by("startTime").ascending());
        Page<Event> eventPage = eventRepository.findUpcomingEvents(LocalDateTime.now(), pageable);
        List<Event> eventList = new ArrayList<>();
        if (eventPage != null) {
            for (Event event : eventPage) {
                Event event1 = new Event();
                event1.setHostId(event.getHostId());
                event1.setTitle(event.getTitle());
                event1.setStartTime(event.getStartTime());
                event1.setLocation(event.getLocation());
                event1.setVisibility(event.getVisibility());
                event1.setDescription(event.getDescription());
                event1.setCreatedAt(event.getCreatedAt());
                event1.setUpdatedAt(event.getUpdatedAt());
                eventList.add(event1);
            }

            return eventList;
        } else {
            return eventList;
        }
    }


}
