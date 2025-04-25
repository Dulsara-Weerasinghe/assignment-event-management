package com.event.service.controller;

import com.event.service.domain.Event;
import com.event.service.dto.*;
import com.event.service.exception.ApplicationException;
import com.event.service.exception.DataNotFounException;
import com.event.service.service.EventService;

import java.util.List;
import java.util.Locale;

import com.event.service.util.EndPoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.naming.ldap.PagedResultsControl;

@RestController
@RequestMapping("/api/1.0/events")
@RequiredArgsConstructor
@Slf4j
public class EventController {

  private final MessageSource messageSource;
  private final EventService eventService;

  @PostMapping(value = EndPoint.CREATE_EVENT , produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> createEvent(Locale locale,
      @RequestBody CreateEventRequest requestBody) {
    try {
      //TODO: implement mapper here to map CreateEventRequest to Event and change the service method
      String eventId = eventService.createEvent(requestBody);
      if (!eventId.isEmpty() && eventId!=null){
        return new ResponseEntity<>(eventId, HttpStatus.OK);
      }else {
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
      }

    } catch (ApplicationException e) {
      ErrorResponse errorResponse = new ErrorResponse(e.getCode(), messageSource.getMessage(e.getMessage(),
          null, locale));
      return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping(value = "/events/{eventId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAnyRole('HOST', 'ADMIN')")
  public ResponseEntity<?> updateEvent(@RequestBody UpdateEvent updateEvent,@PathVariable("eventId") String eventid) throws DataNotFounException {
    log.info("Updating a task {} ", updateEvent.toString());
    return eventService.updateTask(updateEvent,eventid);
  }


  @DeleteMapping(value = "/events/{eventId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAnyRole('HOST', 'ADMIN')")
  public ResponseEntity<?> deleteEvent(@PathVariable("eventId")String eventId) throws DataNotFounException {
    log.info("Updating a task {} ", eventId.toString());
    return eventService.deleteEvent(eventId);
  }


  @PostMapping(value = EndPoint.FILTER_EVENT, produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Event> filterEvents(@RequestBody FilterEvents filterEvents){
    log.debug("Filtering event details " + filterEvents);

    return eventService.filterEvents(filterEvents);
  }


  @PostMapping(value = EndPoint.UPCOMING_EVENTS, produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Event> filterEvents(@RequestBody UpcomingEventsRequest upcomingEventsRequest){
    log.debug("Filtering event details " + upcomingEventsRequest);
    return eventService.upcomingEvents(upcomingEventsRequest);
  }



}
