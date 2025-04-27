package com.event.service.service;

import com.event.service.domain.Event;
import com.event.service.dto.*;
import com.event.service.enums.EventVisibilityType;
import com.event.service.exception.DataNotFounException;
import com.event.service.exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface EventService {

    String createEvent(CreateEventRequest createEventRequest);

    ResponseBean updateTask(UpdateEvent updateEvent, String eventId) throws DataNotFounException;

    ResponseBean deleteEvent(String eventId);

    ResponseBean filterEvents(FilterEvents filterEvents);

    ResponseBean upcomingEvents(UpcomingEventsRequest upcomingEventsRequest);

    String checkEventId(String eventId) throws ResourceNotFoundException;

    Map<String, List<EventDto>> getUserHostedAndAttendingEvents(String userId);

    EventDetailsResponse getEventDetails(String id) throws ResourceNotFoundException;
}
