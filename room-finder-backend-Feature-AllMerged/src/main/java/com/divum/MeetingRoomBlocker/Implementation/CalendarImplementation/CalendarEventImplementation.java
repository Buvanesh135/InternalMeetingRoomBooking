package com.divum.MeetingRoomBlocker.Implementation.CalendarImplementation;
import com.amazonaws.services.resiliencehub.model.SopRecommendation;
import com.divum.MeetingRoomBlocker.DTO.CalendatEventDto.CreateEventDto;
import com.divum.MeetingRoomBlocker.DTO.CalendatEventDto.DeleteEventDto;
import com.divum.MeetingRoomBlocker.DTO.CalendatEventDto.UpdateEventDto;
import com.divum.MeetingRoomBlocker.Entity.MeetingEntity;
import com.divum.MeetingRoomBlocker.Entity.UserEntity;
import com.divum.MeetingRoomBlocker.Repository.MeetingEntityRepository;
import com.divum.MeetingRoomBlocker.Repository.UserEntityRepository;
import com.divum.MeetingRoomBlocker.Service.CalendarService.CalendarEventService;
import com.divum.MeetingRoomBlocker.Service.GoogleCloudApiService.GoogleCloudService;
import com.divum.MeetingRoomBlocker.Util.CreateEventInCalendar;
import com.divum.MeetingRoomBlocker.Util.TokenValidationForEventCalendar;
import com.divum.MeetingRoomBlocker.Util.UpdateEventInCalendar;
import com.google.api.services.calendar.model.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@RequiredArgsConstructor
public class CalendarEventImplementation implements CalendarEventService {
    private final UserEntityRepository userEntityRepository;

    private final TokenValidationForEventCalendar tokenValidationForEventCalendar;

    private final CreateEventInCalendar createEventInCalendar;

    private final GoogleCloudService googleCloudService;

    private final MeetingEntityRepository meetingEntityRepository;

    private final UpdateEventInCalendar updateEventInCalendar;
    @Override
    public ResponseEntity<?> calendarCreateEvent(CreateEventDto createEventDto) {
        UserEntity userEntity;
        Optional<UserEntity> gmailValidate=userEntityRepository.findByEmail(createEventDto.getOrganizerEmail());
        if(gmailValidate.isEmpty())
        {
          throw new UsernameNotFoundException("user not found");
        }
            UserEntity organizerDetails=gmailValidate.get();
            String accessToken=organizerDetails.getAccessToken();
            if(!tokenValidationForEventCalendar.isAccessTokenValid(accessToken))
            {
                 accessToken=tokenValidationForEventCalendar.refreshAccessToken(organizerDetails.getRefreshToken());
                 organizerDetails.setAccessToken(accessToken);
                 userEntityRepository.save(organizerDetails);
            }
        Event event= createEventInCalendar.createEventObject(createEventDto);
        String host=createEventDto.getOrganizerEmail();
        Event createdEvent=googleCloudService.eventCreation(accessToken,event,host);
        String eventId=createdEvent.getId();
        System.out.println(eventId);
        System.out.println(createEventDto.getMeetingId());
        updateMeetingEntityWithEventId(createEventDto.getMeetingId(), eventId);
        return new ResponseEntity<>("Event Created SuccessFully=" + eventId, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> calendarUpdateEvent(String eventId,UpdateEventDto updateEventDto) {
        UserEntity userEntity;
        Optional<UserEntity> gmailValidate=userEntityRepository.findByEmail(updateEventDto.getOrganizerEmail());
        if(gmailValidate.isEmpty())
        {
            throw new UsernameNotFoundException("user not found");
        }
        UserEntity organizerDetails=gmailValidate.get();
        String accessToken=organizerDetails.getAccessToken();
        if(!tokenValidationForEventCalendar.isAccessTokenValid(accessToken))
        {
            accessToken=tokenValidationForEventCalendar.refreshAccessToken(organizerDetails.getRefreshToken());
            organizerDetails.setAccessToken(accessToken);
            userEntityRepository.save(organizerDetails);
        }

        Event existingEvent =googleCloudService.getEvent(organizerDetails.getAccessToken(),eventId,organizerDetails.getEmail());
        Event updatedexistingEvent=updateEventInCalendar.updateExistingEvent(existingEvent,updateEventDto);
        Event updateEventInCalendar=googleCloudService.updateEvent(organizerDetails.getAccessToken(),eventId,organizerDetails.getEmail(),updatedexistingEvent);

        return new ResponseEntity<>("Event Updated", HttpStatus.OK);

    }

    @Override
    public ResponseEntity<?> calendarDeleteEvent(String eventId, DeleteEventDto deleteEventDto) {
        UserEntity userEntity;
        Optional<UserEntity> gmailValidate=userEntityRepository.findByEmail(deleteEventDto.getOrganizerEmail());
        if(gmailValidate.isEmpty())
        {
            throw new UsernameNotFoundException("user not found");
        }
        UserEntity organizerDetails=gmailValidate.get();
        String accessToken=organizerDetails.getAccessToken();
        if(!tokenValidationForEventCalendar.isAccessTokenValid(accessToken))
        {
            accessToken=tokenValidationForEventCalendar.refreshAccessToken(organizerDetails.getRefreshToken());
            organizerDetails.setAccessToken(accessToken);
            userEntityRepository.save(organizerDetails);
        }
        googleCloudService.deleteEventinCalendar(organizerDetails.getAccessToken(),eventId,organizerDetails.getEmail());
        return new ResponseEntity<>("Event deleted Successfully",HttpStatus.OK);

    }
    private void updateMeetingEntityWithEventId(Long meetingId, String eventId) {
        Optional<MeetingEntity> meetingEntityOptional = meetingEntityRepository.findById(meetingId);
        if (meetingEntityOptional.isPresent()) {
            MeetingEntity meetingEntity = meetingEntityOptional.get();
            meetingEntity.setOriginalMeetingId(eventId);
            meetingEntityRepository.save(meetingEntity);
        }
    }
}
