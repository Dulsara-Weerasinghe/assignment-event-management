package com.event.service.test;

import com.event.service.domain.Event;
import com.event.service.dto.EventDetailsResponse;
import com.event.service.dto.EventDto;
import com.event.service.dto.UpcomingEventsRequest;
import com.event.service.enums.EventVisibilityType;
import com.event.service.exception.ResourceNotFoundException;
import com.event.service.repository.AttendenceRepository;
import com.event.service.repository.EventRepository;
import com.event.service.service.impl.EventServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class EventTestService {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private AttendenceRepository attendanceRepository;

    @InjectMocks
    private EventServiceImpl eventService;

    @Test
    void testGetEventDetails() throws ResourceNotFoundException {
        UUID eventId = UUID.randomUUID();
        Event event = new Event();
        event.setId(String.valueOf(eventId));
        event.setTitle("Test Event");
        event.setDescription("Test Desc");
        event.setLocation("Test Location");
        event.setStartTime(LocalDateTime.now());
        event.setEndTime(LocalDateTime.now().plusHours(2));
        event.setVisibility(EventVisibilityType.valueOf(EventVisibilityType.PUBLIC.name()));

        Mockito.when(eventRepository.findById(String.valueOf(eventId))).thenReturn(Optional.of(event));
        Mockito.when(attendanceRepository.countAttendeesByEventId(String.valueOf(eventId))).thenReturn(5L);

        EventDetailsResponse eventDetails = eventService.getEventDetails(String.valueOf(eventId));

        assertEquals(eventId, eventDetails.getId());
        assertEquals("Test Event", eventDetails.getTitle());
        assertEquals(5L, eventDetails.getAttendeeCount());
    }


    /**
     * GetUpcoming events test case
     */
    @Test
    void getUpcomingEvents() throws ResourceNotFoundException {
        Pageable pageable = PageRequest.of(0, 5);
        Event event = new Event();
        event.setId(String.valueOf(UUID.randomUUID()));
        event.setTitle("test title");

        Page<Event> eventPage = new PageImpl<>(List.of(event));

        Mockito.when(eventRepository.findUpcomingEvents(Mockito.any(LocalDateTime.class), Mockito.eq(pageable)))
                .thenReturn(eventPage);

        UpcomingEventsRequest upcomingEventsRequest = new UpcomingEventsRequest();
        upcomingEventsRequest.setPage(0);
        upcomingEventsRequest.setSize(5);
        upcomingEventsRequest.setDate(LocalDateTime.now());
        //when
//        List<Event> result = eventService.upcomingEvents(upcomingEventsRequest);

        // Then
//        assertEquals(1, result.size());
//        assertEquals("Upcoming Event", result.get(0).getTitle());

    }

    /**
     * Get event status test case
     **/

    @Test
    void checkStatus() throws ResourceNotFoundException {
        String eventId = String.valueOf(UUID.randomUUID());
        Event event = new Event();
        event.setId(eventId);
        event.setStartTime(LocalDateTime.now().plusDays(1));
        event.setEndTime(LocalDateTime.now().plusDays(2));

        //when
        Mockito.when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        String status = eventService.checkEventId(eventId);

        //Then
        assertEquals("UPCOMING",status);
    }


    @Test
    void testGetEventStatus_Ongoing() throws ResourceNotFoundException {
        String eventId = String.valueOf(UUID.randomUUID());
        Event event = new Event();
        event.setId(eventId);
        event.setStartTime(LocalDateTime.now().minusHours(1));
        event.setEndTime(LocalDateTime.now().plusHours(1));

        Mockito.when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        String status = String.valueOf(eventService.getEventDetails(eventId));

        assertEquals("ONGOING", status);
    }


    @Test
    void testGetEventStatus_Completed() throws ResourceNotFoundException {
        String eventId = String.valueOf(UUID.randomUUID());
        Event event = new Event();
        event.setId(eventId);
        event.setStartTime(LocalDateTime.now().minusDays(2));
        event.setEndTime(LocalDateTime.now().minusDays(1));

        Mockito.when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        String status = String.valueOf(eventService.getEventDetails(eventId));

        assertEquals("COMPLETED", status);
    }


    @Test
    void testEventWithAttendeeCount() throws ResourceNotFoundException {
        String eventId = String.valueOf(UUID.randomUUID());

        Event event = new Event();
        event.setId(eventId);
        event.setTitle("events");


        Mockito.when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        Mockito.when(attendanceRepository.countAttendeesByEventId(eventId)).thenReturn(10L);

        EventDetailsResponse response = eventService.getEventDetails(eventId);

        assertEquals(eventId,response.getId());
        assertEquals("Test event",response.getTitle());
//        assertEquals(10L);
    }



}
