package com.divum.MeetingRoomBlocker.Util;

import com.divum.MeetingRoomBlocker.DTO.CalendatEventDto.CreateEventDto;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class CreateEventInCalendar {
    public Event createEventObject(CreateEventDto createEventDto)
    {
        Event event = new Event();
        event.setSummary(createEventDto.getSummary());
        EventDateTime start = new EventDateTime().setDateTime(new DateTime(createEventDto.getStartTime()));
        EventDateTime end = new EventDateTime().setDateTime(new DateTime(createEventDto.getEndTime()));
        event.setStart(start);
        event.setEnd(end);
        event.setDescription(createEventDto.getDescription());
        event.setAttendees(new ArrayList<>());
        EventAttendee organizer = new EventAttendee().setEmail(createEventDto.getOrganizerEmail()).setOrganizer(true);
        event.getAttendees().add(organizer);
        for (String attendeeEmail : createEventDto.getAttendees()) {
            EventAttendee attendee = new EventAttendee().setEmail(attendeeEmail);
            event.getAttendees().add(attendee);
        }
        return event;
    }


}