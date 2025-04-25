package com.event.service.service;

import com.event.service.domain.Event;
import com.event.service.dto.*;
import com.event.service.enums.EventVisibilityType;
import com.event.service.exception.DataNotFounException;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

  String createEvent(CreateEventRequest createEventRequest);

    ResponseEntity<?> updateTask(UpdateEvent updateEvent,String eventId) throws DataNotFounException;

  ResponseEntity<?> deleteEvent(String eventId);

  List<Event> filterEvents(FilterEvents filterEvents);

  List<Event> upcomingEvents(UpcomingEventsRequest upcomingEventsRequest);
}
