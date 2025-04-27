package com.event.service.dto;

import com.event.service.enums.EventVisibilityType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDto {


    public EventDto(String id, String title, String description, LocalDateTime startTime, LocalDateTime endTime, String location, EventVisibilityType visibility) {
    }
}
