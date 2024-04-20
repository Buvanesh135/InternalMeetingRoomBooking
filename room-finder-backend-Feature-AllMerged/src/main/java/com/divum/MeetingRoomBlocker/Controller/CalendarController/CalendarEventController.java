package com.divum.MeetingRoomBlocker.Controller.CalendarController;

import com.divum.MeetingRoomBlocker.DTO.CalendatEventDto.CreateEventDto;
import com.divum.MeetingRoomBlocker.DTO.CalendatEventDto.DeleteEventDto;
import com.divum.MeetingRoomBlocker.DTO.CalendatEventDto.UpdateEventDto;
import com.divum.MeetingRoomBlocker.Service.CalendarService.CalendarEventService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
//@RequestMapping("/v1/api/calendar/event/")
@AllArgsConstructor
public class CalendarEventController {

    private final CalendarEventService calendarEventService;
//@PostMapping("createEvent")
    public ResponseEntity<?> calendarCreateEvent(@RequestBody CreateEventDto createEventDto)
{
 return calendarEventService.calendarCreateEvent(createEventDto);
}
//@PutMapping("updateEvent")
    public ResponseEntity<?> calendarUpdateEvent(@RequestParam("eventId") String eventId,@RequestBody UpdateEventDto updateEventDto)
{
    return calendarEventService.calendarUpdateEvent(eventId,updateEventDto);
}
//@DeleteMapping("deleteEvent")
    public ResponseEntity<?> calendarDeleteEvent(@RequestParam ("eventId") String eventId, @RequestBody DeleteEventDto deleteEventDto)
{
    return calendarEventService.calendarDeleteEvent(eventId,deleteEventDto);
}
}
