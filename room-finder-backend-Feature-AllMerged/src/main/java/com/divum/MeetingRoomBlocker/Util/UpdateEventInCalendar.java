package com.divum.MeetingRoomBlocker.Util;

import com.divum.MeetingRoomBlocker.DTO.CalendatEventDto.UpdateEventDto;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UpdateEventInCalendar {
    public static Event updateExistingEvent(Event existingEvent, UpdateEventDto updateEventDto) {
        if (updateEventDto.getAttendeesToAdd() != null && !updateEventDto.getAttendeesToAdd().isEmpty()) {
            List<EventAttendee> attendeesToAdd = new ArrayList<>();
            for (String attendeeEmail : updateEventDto.getAttendeesToAdd()) {
                attendeesToAdd.add(new EventAttendee().setEmail(attendeeEmail));
            }
            existingEvent.getAttendees().addAll(attendeesToAdd);
        }
        if (updateEventDto.getAttendeesToRemove() != null && !updateEventDto.getAttendeesToRemove().isEmpty()) {
            List<EventAttendee> attendeesToRemove = new ArrayList<>();
            for (String attendeeEmail : updateEventDto.getAttendeesToRemove()) {
                for (EventAttendee attendee : existingEvent.getAttendees()) {
                    if (attendee.getEmail().equals(attendeeEmail)) {
                        attendeesToRemove.add(attendee);
                        break;
                    }
                }
            }
            existingEvent.getAttendees().removeAll(attendeesToRemove);
        }

        return existingEvent;
    }
}
