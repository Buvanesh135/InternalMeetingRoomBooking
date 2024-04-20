package com.divum.MeetingRoomBlocker.Service.CalendarService;

import com.divum.MeetingRoomBlocker.DTO.CalendatEventDto.CreateEventDto;
import com.divum.MeetingRoomBlocker.DTO.CalendatEventDto.DeleteEventDto;
import com.divum.MeetingRoomBlocker.DTO.CalendatEventDto.UpdateEventDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface CalendarEventService {
   public ResponseEntity<?> calendarCreateEvent(CreateEventDto createEventDto);
   public ResponseEntity<?> calendarUpdateEvent(String eventId,UpdateEventDto updateEventDto);

   ResponseEntity<?> calendarDeleteEvent(String eventId, DeleteEventDto deleteEventDto);
}
