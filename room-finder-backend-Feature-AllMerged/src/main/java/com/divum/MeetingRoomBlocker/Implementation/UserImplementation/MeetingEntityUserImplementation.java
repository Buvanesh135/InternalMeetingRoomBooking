package com.divum.MeetingRoomBlocker.Implementation.UserImplementation;

import com.divum.MeetingRoomBlocker.DTO.*;
import com.divum.MeetingRoomBlocker.DTO.CalendatEventDto.DeleteEventDto;
import com.divum.MeetingRoomBlocker.Entity.Enum.MeetingStatusEntity;
import com.divum.MeetingRoomBlocker.Entity.MeetingEntity;
import com.divum.MeetingRoomBlocker.Entity.UserEntity;
import com.divum.MeetingRoomBlocker.Exception.DataNotFoundException;
import com.divum.MeetingRoomBlocker.Exception.InvalidDataException;
import com.divum.MeetingRoomBlocker.Exception.InvalidEmailException;
import com.divum.MeetingRoomBlocker.Repository.*;
import com.divum.MeetingRoomBlocker.Service.CalendarService.CalendarEventService;
import com.divum.MeetingRoomBlocker.Service.MailServices.MailService;
import com.divum.MeetingRoomBlocker.Service.UserService.MeetingEntityUserService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Builder
@RequiredArgsConstructor
public class MeetingEntityUserImplementation implements MeetingEntityUserService {
    private final MeetingCategoryEntityRepository meetingCategoryEntityRepository;
    private final MeetingEntityRepository meetingEntityRepository;
    private final UserEntityRepository userEntityRepository;
    private final RoomEntityRepository roomEntityRepository;
    private final CalendarEventService calendarEventService;
    private final MailService mailService;


    //BookMeeting
    @Override
    public ResponseDTO addMeeting(MeetingEntityRequestDTO meetingEntity) {
        try {
            List<UserEntity> internalAttendees=userEntityRepository.findByEmailIn(meetingEntity.getUserEntityList());

            if(internalAttendees.isEmpty()){
                throw new InvalidEmailException("Email not found ");
            }
            LocalDateTime startTime = LocalDateTime.parse(meetingEntity.getStartTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
            LocalDateTime endTime = LocalDateTime.parse(meetingEntity.getEndTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
            Timestamp startTimestamp = Timestamp.valueOf(startTime);
            Timestamp endTimestamp = Timestamp.valueOf(endTime);
            int date=startTimestamp.compareTo(endTimestamp);
            if(date>0){
               throw new InvalidDataException("Start time is greater than end time");
            }
            String email=SecurityContextHolder.getContext().getAuthentication().getName();
            var newMeeting = MeetingEntity.builder()
                    .meetingName(meetingEntity.getMeetingName())
                    .startTime(startTimestamp)
                    .endTime(endTimestamp)
                    .meetingCategoryEntity(meetingCategoryEntityRepository.findMeetingCategoryEntities(meetingEntity.getMeetingCategory()))
                    .description(meetingEntity.getDescription())
                    .guestList(internalAttendees)
                    .host(userEntityRepository.findByEmail(email).orElseThrow(()-> new InvalidEmailException("Email Not found") ))
                    .roomEntity(roomEntityRepository.findById(meetingEntity.getRoomEntityId()).orElseThrow(()->new DataNotFoundException("Room not found")))
                    .status(MeetingStatusEntity.PENDING)
                    .build();
            meetingEntityRepository.save(newMeeting);
//            List<MeetingEntity> meetingEntityList=new ArrayList<>();
//            meetingEntityList.add(newMeeting);
           //for task scheduler method calling
//            List<MeetingEntityActivitiesDTO> meetingDTOs = meetingEntityList.stream()
//                    .map(this::convertToActivitiesDTO)
//                    .collect(Collectors.toList());
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.CREATED.getReasonPhrase())
                    .message("Your booking is confirmed ")
                    .data("Booking Confirmed")
                    .build();
        } catch (InvalidEmailException e) {
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.NOT_FOUND.getReasonPhrase())
                    .message("Email is invalid")
                    .build();
        } catch (InvalidDataException e) {
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.NOT_FOUND.getReasonPhrase())
                    .message("Data is invalid")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                    .message("Booking is unavailable at the moment")
                    .build();
        }


    }

    @Override
    public ResponseDTO getMeetingDetails(Long id) {
        try {

            MeetingEntity meetingEntity = meetingEntityRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Meeting Not found"));
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.OK.getReasonPhrase())
                    .message("Meeting Details")
                    .data(meetingEntity)
                    .build();
        } catch (DataNotFoundException e) {
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.NOT_FOUND.getReasonPhrase())
                    .message("Meeting is unavailable")
                    .build();
        } catch (Exception e) {
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                    .message("Booking is unavailable at the moment")
                    .build();
        }
    }

    //editMeeting
    @Override
    public ResponseDTO editMeetingDetails(Long id, MeetingEntityRequestDTO meetingEntity) {
        try {
            List<UserEntity> internalAttendees=userEntityRepository.findByEmailIn(meetingEntity.getUserEntityList());
            if(internalAttendees.isEmpty()){
                throw new InvalidEmailException("Email not found ");
            }
            String email=SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity hostDetails=userEntityRepository.findByEmail(email).orElseThrow(()->new InvalidEmailException("Email not found for Host"));
            MeetingEntity meeting = meetingEntityRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Meeting Not found"));
            LocalDateTime startTime = LocalDateTime.parse(meetingEntity.getStartTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
            LocalDateTime endTime = LocalDateTime.parse(meetingEntity.getEndTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
            Timestamp startTimestamp = Timestamp.valueOf(startTime);
            Timestamp endTimestamp = Timestamp.valueOf(endTime);
            if (meeting.getStatus().equals(MeetingStatusEntity.PENDING) && meeting.getHost().getEmail().equals(email)) {
                meeting = MeetingEntity.builder()
                        .id(meeting.getId())
                        .meetingName(meetingEntity.getMeetingName())
                        .startTime(startTimestamp)
                        .endTime(endTimestamp)
                        .meetingCategoryEntity(meetingCategoryEntityRepository.findMeetingCategoryEntities(meetingEntity.getMeetingCategory()))
                        .description(meetingEntity.getDescription())
                        .guestList(internalAttendees)
                        .host(hostDetails)
                        .roomEntity(roomEntityRepository.findById(meetingEntity.getRoomEntityId()).orElseThrow(()->new DataNotFoundException("Room not found")))
                        .status(MeetingStatusEntity.PENDING)
                        .build();

                meetingEntityRepository.save(meeting);
                List<MeetingEntity> meetingEntityList=new ArrayList<>();
                meetingEntityList.add(meeting);


                List<MeetingEntityActivitiesDTO> meetingDTOs = meetingEntityList.stream()
                        .map(this::convertToActivitiesDTO)
                        .collect(Collectors.toList());

                return ResponseDTO.builder()
                        .httpStatus(HttpStatus.OK.getReasonPhrase())
                        .message("Meeting has been updated Successfully")
                        .data(meetingDTOs)
                        .build();
            }
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.FORBIDDEN.getReasonPhrase())
                    .message("Has no permission to edit meeting")
                    .data("[]")
                    .build();
        } catch (DataNotFoundException e) {
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.NOT_FOUND.getReasonPhrase())
                    .message("Meeting is unavailable")
                    .build();
        } catch (InvalidDataException e) {
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST.getReasonPhrase())
                    .message("Meeting is unavailable")
                    .build();

        } catch (Exception e) {
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                    .message("Booking is unavailable at the moment")
                    .build();
        }
    }

    //UserDashBoardApi
    @Override
    public ResponseDTO getUserMeetingDetails( Date date) {
        try {
            String email= SecurityContextHolder.getContext().getAuthentication().getName();
            Long userId = userEntityRepository.findByEmail(email).orElseThrow(() -> new InvalidEmailException(email)).getId();
            System.out.println(email);
            List<MeetingEntity> meetingDetails = meetingEntityRepository.findMeetingsByUserEmailAndDate(date,MeetingStatusEntity.ACCEPTED, userId);

            List<MeetingEntityResponseDTO> meetingDTOs = meetingDetails.stream()
                    .map(this::convertToUserDTO)
                    .collect(Collectors.toList());
            return ResponseDTO.builder()
                    .data(meetingDTOs)
                    .message("Details")
                    .httpStatus(HttpStatus.OK.getReasonPhrase())
                    .build();
        } catch (InvalidEmailException e) {
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.NOT_FOUND.getReasonPhrase())
                    .message("Email not found")
                    .build();
        } catch (DataNotFoundException e) {
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.NOT_FOUND.getReasonPhrase())
                    .message("Meeting is unavailable")
                    .build();
        } catch (Exception e) {
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                    .message("Error in fetching meeting")
                    .build();
        }

    }
    //UpcomingAndPendingMeetingForAttendee
    @Override
    public ResponseDTO getUpcomingMeetingDetails( LocalDateTime date, MeetingStatusEntity status) {
        try {
            if(date==null){
                date=LocalDateTime.now();
            }
            String email= SecurityContextHolder.getContext().getAuthentication().getName();
            Long userId = userEntityRepository.findByEmail(email).orElseThrow(() -> new InvalidEmailException(email)).getId();
            if(!(status.equals(MeetingStatusEntity.ACCEPTED) || status.equals(MeetingStatusEntity.PENDING))){
               throw  new InvalidDataException("Invalid Status") ;
            }
            if(status.equals(MeetingStatusEntity.ACCEPTED)){
            List<MeetingEntity> upcomingMeeting = meetingEntityRepository.findByHostIdAndDateAndUpcomingMeeting(date, status, userId);
            if (upcomingMeeting.isEmpty()) {
                throw new DataNotFoundException("Meetings not found");
            }
            List<MeetingEntityActivitiesDTO> upcomingMeetingList = upcomingMeeting.stream()
                    .map(this::convertToActivitiesDTO)
                    .collect(Collectors.toList());
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.OK.getReasonPhrase())
                    .data(upcomingMeetingList)
                    .message("success")
                    .build();}
            else {
                List<MeetingEntity> upcomingMeeting = meetingEntityRepository.findByHostIdAndPendingMeeting(date, status, userId);
                if (upcomingMeeting.isEmpty()) {
                    throw new DataNotFoundException("Meetings not found");
                }
                List<MeetingEntityActivitiesDTO> upcomingMeetingList = upcomingMeeting.stream()
                        .map(this::convertToActivitiesDTO)
                        .collect(Collectors.toList());
                return ResponseDTO.builder()
                        .httpStatus(HttpStatus.OK.getReasonPhrase())
                        .data(upcomingMeetingList)
                        .message("success")
                        .build();
            }

        } catch (InvalidEmailException e) {
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.NOT_FOUND.getReasonPhrase())
                    .message("Email not found")
                    .build();
        } catch (InvalidDataException e) {
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.NOT_FOUND.getReasonPhrase())
                    .message("Invalid Status")
                    .build();
        }
        catch (DataNotFoundException e) {
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.NOT_FOUND.getReasonPhrase())
                    .message("Meeting is unavailable")
                    .build();
        } catch (Exception e) {
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                    .message("Error in fetching meeting")
                    .build();
        }
    }
    //GetHostCompletedMeeting
    @Override
    public ResponseDTO getCompletedMeeting( LocalDateTime date) {
        try{
            if(date==null){
                date=LocalDateTime.now();
            }

            String email= SecurityContextHolder.getContext().getAuthentication().getName();
            Long userId = userEntityRepository.findByEmail(email).orElseThrow(() -> new InvalidEmailException(email)).getId();
            List<MeetingEntity> upcomingMeeting = meetingEntityRepository.findByHostIdAndDateAndCompletedMeeting(date,MeetingStatusEntity.COMPLETED,MeetingStatusEntity.REJECTED, userId);
            if (upcomingMeeting.isEmpty()) {
                throw new DataNotFoundException("Meetings not found");
            }
            List<MeetingActivityCompletedDTO> upcomingMeetingList = upcomingMeeting.stream()
                    .map(this::convertToCompletedActivitiesDTO)
                    .collect(Collectors.toList());
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.OK.getReasonPhrase())
                    .data(upcomingMeetingList)
                    .message("success")
                    .build();
        }catch (InvalidEmailException e) {
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.NOT_FOUND.getReasonPhrase())
                    .message("Email not found")
                    .build();
        } catch (DataNotFoundException e) {
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.NOT_FOUND.getReasonPhrase())
                    .message("Meeting is unavailable")
                    .build();
        } catch (Exception e) {
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                    .message("Error in fetching meeting")
                    .build();
        }

    }

    @Override
    public ResponseDTO withdrawMeeting(Long id) {

        try{
            MeetingEntity meetingEntity=meetingEntityRepository.findById(id).orElseThrow(()->new DataNotFoundException("Meeting not found"));
            String email=SecurityContextHolder.getContext().getAuthentication().getName();
            if(email.equals(meetingEntity.getHost().getEmail())) {
                MeetingStatusEntity updatedStatus=meetingEntity.getStatus().equals(MeetingStatusEntity.PENDING)?MeetingStatusEntity.PENDING:MeetingStatusEntity.CANCELLED;
                var updatedMeeting = MeetingEntity.builder()
                        .id(id)
                        .meetingName(meetingEntity.getMeetingName())
                        .startTime(meetingEntity.getStartTime())
                        .endTime(meetingEntity.getEndTime())
                        .meetingCategoryEntity(meetingCategoryEntityRepository.findMeetingCategoryEntities(meetingEntity.getMeetingName()))
                        .description(meetingEntity.getDescription())
                        .guestList(meetingEntity.getGuestList())
                        .host(meetingEntity.getHost())
                        .roomEntity(meetingEntity.getRoomEntity())
                        .status(updatedStatus)
                        .isDeleted(true)
                        .build();
                meetingEntityRepository.save(updatedMeeting);
                if(updatedStatus.equals(MeetingStatusEntity.CANCELLED)){
                    Long meetingId = updatedMeeting.getId();
                    String reason = updatedMeeting.getReason();
                    mailService.MeetingCancel(meetingId, reason);
                    ResponseEntity<?> calendarResponse = deleteEventFromCalendar(meetingEntity);
                    if (!calendarResponse.getStatusCode().is2xxSuccessful()) {
                        return ResponseDTO.builder()
                                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                                .message("Meeting is deleted but failed to delete from Google Calendar.")
                                .build();
                    }
                }

                return ResponseDTO.builder()
                        .httpStatus(HttpStatus.NO_CONTENT.getReasonPhrase())
                        .message("Meeting  is deleted ")
                        .data("[]")
                        .build();

            }
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                    .message("Error")
                    . build();

        }catch (DataNotFoundException e) {
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.NOT_FOUND.getReasonPhrase())
                    .message("Meeting is unavailable")
                    .build();
        } catch (Exception e) {
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                    .message("Error in fetching meeting")
                    .build();
        }


    }

    private ResponseEntity<?> deleteEventFromCalendar(MeetingEntity meeting) {
        try {
            String eventId = meeting.getOriginalMeetingId();
            String organizerEmail = meeting.getHost().getEmail();
            DeleteEventDto deleteEventDto = new DeleteEventDto(organizerEmail);
            return calendarEventService.calendarDeleteEvent(eventId, deleteEventDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting event from Google Calendar");
        }
    }



    private MeetingEntityResponseDTO convertToUserDTO(MeetingEntity meetingEntity) {
        LocalDateTime startTime = meetingEntity.getStartTime().toLocalDateTime();
        LocalDateTime endTime = meetingEntity.getEndTime().toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        DateTimeFormatter dateFormatter=DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return MeetingEntityResponseDTO.builder()
                .meetingName(meetingEntity.getMeetingName())
                .startDate(startTime.toLocalDate().format(dateFormatter))
                .endDate(endTime.toLocalDate().format(dateFormatter))
                .startTime(startTime.toLocalTime().format(formatter))
                .endTime(endTime.toLocalTime().format(formatter))
                .roomName(meetingEntity.getRoomEntity().getName())
                .hostName(meetingEntity.getHost().getName())
                .meetingCategory(meetingEntity.getMeetingCategoryEntity().getCategoryName())
                .build();
    }
    private MeetingEntityActivitiesDTO convertToActivitiesDTO(MeetingEntity meetingEntity) {
        LocalDateTime startTime = meetingEntity.getStartTime().toLocalDateTime();
        LocalDateTime endTime = meetingEntity.getEndTime().toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        DateTimeFormatter dateFormatter=DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return MeetingEntityActivitiesDTO.builder()
                .id(meetingEntity.getId())
                .meetingName(meetingEntity.getMeetingName())
                .description(meetingEntity.getDescription())
                .startDate(startTime.toLocalDate().format(dateFormatter))
                .endDate(endTime.toLocalDate().format(dateFormatter))
                .startTime(startTime.toLocalTime().format(formatter))
                .endTime(endTime.toLocalTime().format(formatter))
                .description(meetingEntity.getDescription())
                .status(meetingEntity.getStatus())
                .roomName(meetingEntity.getRoomEntity().getName())
                .hostName(meetingEntity.getHost().getName())
                .hostEmail(meetingEntity.getHost().getEmail())
                .meetingCategory(meetingEntity.getMeetingCategoryEntity().getCategoryName())
                .guestList(meetingEntity.getGuestList() .stream()
                        .map(this::covertToUserList)
                        .collect(Collectors.toList()))
                .build();
    }
    private MeetingActivityCompletedDTO convertToCompletedActivitiesDTO(MeetingEntity meetingEntity) {
        LocalDateTime startTime = meetingEntity.getStartTime().toLocalDateTime();
        LocalDateTime endTime = meetingEntity.getEndTime().toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        DateTimeFormatter dateFormatter=DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return MeetingActivityCompletedDTO.builder()
                .meetingName(meetingEntity.getMeetingName())
                .startDate(startTime.toLocalDate().format(dateFormatter))
                .endDate(endTime.toLocalDate().format(dateFormatter))
                .startTime(startTime.toLocalTime().format(formatter))
                .endTime(endTime.toLocalTime().format(formatter))
                .roomName(meetingEntity.getRoomEntity().getName())
                .hostName(meetingEntity.getHost().getName())
                .hostEmail(meetingEntity.getHost().getEmail())
                .status(meetingEntity.getStatus())
                .description(meetingEntity.getDescription())
                .reason(meetingEntity.getReason())
                .meetingCategory(meetingEntity.getMeetingCategoryEntity().getCategoryName())
                .guestList(meetingEntity.getGuestList() .stream()
                        .map(this::covertToUserList)
                        .collect(Collectors.toList()))
                .build();
    }
    private InternalUserDTO covertToUserList(UserEntity userEntity) {
        return InternalUserDTO.builder()
                .email(userEntity.getEmail())
                .name(userEntity.getName())
                .build();

    }

}
