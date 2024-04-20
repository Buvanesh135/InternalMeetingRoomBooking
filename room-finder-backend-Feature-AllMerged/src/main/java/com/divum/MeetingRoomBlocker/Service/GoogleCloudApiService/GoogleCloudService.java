package com.divum.MeetingRoomBlocker.Service.GoogleCloudApiService;

import com.google.api.services.calendar.model.Event;
import jakarta.validation.constraints.Email;
import org.springframework.stereotype.Service;

@Service
public interface GoogleCloudService {

    Event eventCreation(String accessToken, Event event, String host);

    Event getEvent(String accessToken, String eventId, String email);

    Event updateEvent(String accessToken, String eventId,@Email String email, Event updatedexistingEvent);

    void deleteEventinCalendar(String accessToken, String eventId, String email);
}
