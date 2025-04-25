package com.event.service.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity

@Table(name = "attendance")
public class Attendance {

  @Id
  @Column(name = "event_id", nullable = false, length = 64)
  private String eventId;

  @Id
  @Column(name = "user_id", nullable = false, length = 64)
  private String userId;

  @Column(name = "status", nullable = false, length = 32)
  private String status;

  @Column(name = "responded_at")
  private LocalDateTime respondedAt;
}
