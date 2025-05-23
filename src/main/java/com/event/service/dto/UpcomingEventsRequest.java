package com.event.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpcomingEventsRequest {

    private int page;
    private int size;
    private LocalDateTime date;

}
