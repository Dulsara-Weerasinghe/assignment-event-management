package com.event.service.repository;

import com.event.service.domain.Attendance;
import com.event.service.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendenceRepository extends JpaRepository<Attendance,String> {
    List<Event> findEventsByUserId(String userId);


    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.eventId = :eventId AND a.status = 'GOING'")
    long countAttendeesByEventId(@Param("eventId") String eventId);
}
