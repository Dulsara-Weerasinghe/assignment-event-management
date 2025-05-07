package com.event.service.test;

import com.event.service.controller.EventController;
import com.event.service.dto.EventDetailsResponse;
import com.event.service.enums.EventVisibilityType;
import com.event.service.service.EventService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventController.class)
public class EventTestController {
    private MockMvc mockMvc;


    private EventService eventService;

    @Test
    void testGetEventDetails() throws Exception {
        String eventId = String.valueOf(UUID.randomUUID());
        EventDetailsResponse eventDetailsResponse = new EventDetailsResponse(eventId,
                "test event","test desc", LocalDateTime.now(),LocalDateTime.now().plusHours(2),"Test location",
                EventVisibilityType.PUBLIC,5);

        Mockito.when(eventService.getEventDetails(eventId)).thenReturn(eventDetailsResponse);


        mockMvc.perform(get("/api/events/" + eventId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(eventId.toString()))
                .andExpect(jsonPath("$.title").value("Test Event"))
                .andExpect(jsonPath("$.attendeeCount").value(5));
    }
}
