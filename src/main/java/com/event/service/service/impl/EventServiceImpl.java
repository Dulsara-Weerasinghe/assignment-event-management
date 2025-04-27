package com.event.service.service.impl;

import com.event.service.domain.Event;
import com.event.service.dto.*;
import com.event.service.enums.EventVisibilityType;
import com.event.service.exception.ApplicationException;
import com.event.service.exception.DataNotFounException;
import com.event.service.exception.ResourceNotFoundException;
import com.event.service.repository.AttendenceRepository;
import com.event.service.repository.EventRepository;
import com.event.service.service.EventService;
import com.event.service.util.ErrorDsc;
import com.event.service.util.EventUtil;
import com.event.service.util.varList.MessageVarList;
import com.event.service.util.varList.StatusVarList;
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
import java.util.Map;

@Service
@Slf4j
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    private final AttendenceRepository attendenceRepository;

    public EventServiceImpl(EventRepository eventRepository, AttendenceRepository attendenceRepository) {
        this.eventRepository = eventRepository;
        this.attendenceRepository = attendenceRepository;
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
    public ResponseBean updateTask(UpdateEvent updateEvent, String eventId) throws DataNotFounException {
        log.debug("Event update request" + updateEvent);

        Event task = eventRepository.findById(eventId).orElseThrow(() -> new DataNotFounException(ErrorDsc.ERR_DSC_TASK_NOT_FOUND));
        task.setDescription(updateEvent.getDescription());
        task.setVisibility(EventVisibilityType.valueOf(updateEvent.getVisibility()));
        task.setLocation(updateEvent.getLocation());
        task.setTitle(updateEvent.getTitle());
        task.setHostId(updateEvent.getHostId());
        Event save = eventRepository.save(task);


        return new ResponseBean(MessageVarList.RSP_SUCCESS, StatusVarList.SUCCESS,save.getId());
    }

    @Override
    public ResponseBean deleteEvent(String eventId) {
        eventRepository.deleteById(eventId);

        return new ResponseBean(MessageVarList.RSP_SUCCESS,StatusVarList.SUCCESSFULLY_DELETED,eventId);
    }

    @Override
    public ResponseBean filterEvents(FilterEvents filterEvents) {

        List<Event> eventList = eventRepository.getList(filterEvents.getDate(), filterEvents.getLocation(), filterEvents.getVisibility());
        if (!eventList.isEmpty()) {
            return new ResponseBean(MessageVarList.RSP_SUCCESS,StatusVarList.SUCCESS,eventList);
        } else {
            return new ResponseBean(MessageVarList.RSP_NO_DATA_FOUND,StatusVarList.FAILED,null);
        }

    }

    @Override
    public ResponseBean upcomingEvents(UpcomingEventsRequest upcomingEventsRequest) {
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

            return new ResponseBean(MessageVarList.RSP_SUCCESS,StatusVarList.SUCCESS,eventList);
        } else {
            return new ResponseBean(MessageVarList.RSP_NO_DATA_FOUND,StatusVarList.FAILED,null);
        }
    }

    @Override
    public String checkEventId(String eventId) throws ResourceNotFoundException {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(event.getStartTime())) {
            return "UPCOMING";
        } else if (now.isAfter(event.getEndTime())) {
            return "COMPLETED";
        } else {
            return "ONGOING";
        }

    }

    @Override
    public Map<String, List<EventDto>> getUserHostedAndAttendingEvents(String userId) {
        List<EventDto> hostedEvents = convertEventsToDtos(eventRepository.findByHostId(userId));
        List<EventDto> attendingEvents = convertEventsToDtos(attendenceRepository.findEventsByUserId(userId));

        return Map.of(
                "hosting", hostedEvents,
                "attending", attendingEvents
        );

    }

    @Override
    public EventDetailsResponse getEventDetails(String id) throws ResourceNotFoundException {


        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        long attendeeCount = attendenceRepository.countAttendeesByEventId(id);

        return new EventDetailsResponse(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getStartTime(),
                event.getEndTime(),
                event.getLocation(),
                EventVisibilityType.valueOf(event.getVisibility().name()),
                attendeeCount
        );


    }

    private List<EventDto> convertEventsToDtos(List<Event> events) {
        return events.stream()
                .map(event -> new EventDto(
                        event.getId(),
                        event.getTitle(),
                        event.getDescription(),
                        event.getStartTime(),
                        event.getEndTime(),
                        event.getLocation(),
                        event.getVisibility()
                ))
                .toList();
    }


}
