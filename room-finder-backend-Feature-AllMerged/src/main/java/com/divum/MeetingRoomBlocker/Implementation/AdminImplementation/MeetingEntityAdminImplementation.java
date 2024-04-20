package com.divum.MeetingRoomBlocker.Implementation.AdminImplementation;

import com.divum.MeetingRoomBlocker.DTO.CalendatEventDto.CreateEventDto;
import com.divum.MeetingRoomBlocker.DTO.CalendatEventDto.DeleteEventDto;
import com.divum.MeetingRoomBlocker.DTO.MeetingEntityActivitiesDTO;
import com.divum.MeetingRoomBlocker.DTO.MeetingEntityDTO;
import com.divum.MeetingRoomBlocker.DTO.MeetingEntityRequestDTO;
import com.divum.MeetingRoomBlocker.DTO.ResponseDTO;
import com.divum.MeetingRoomBlocker.Entity.Enum.MeetingStatusEntity;
import com.divum.MeetingRoomBlocker.Entity.MeetingEntity;
import com.divum.MeetingRoomBlocker.Entity.RoomEntity;
import com.divum.MeetingRoomBlocker.Entity.UserEntity;
import com.divum.MeetingRoomBlocker.Exception.DataNotFoundException;
import com.divum.MeetingRoomBlocker.Exception.InvalidDataException;
import com.divum.MeetingRoomBlocker.Exception.InvalidEmailException;
import com.divum.MeetingRoomBlocker.Repository.MeetingEntityRepository;
import com.divum.MeetingRoomBlocker.Repository.RoomEntityRepository;
import com.divum.MeetingRoomBlocker.Repository.UserEntityRepository;
import com.divum.MeetingRoomBlocker.Scheduler.TaskTrigger;
import com.divum.MeetingRoomBlocker.Service.AdminService.MeetingEntityAdminService;
import com.divum.MeetingRoomBlocker.Service.CalendarService.CalendarEventService;
import com.divum.MeetingRoomBlocker.Service.MailServices.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.http.ResponseEntity.status;

@Service
@RequiredArgsConstructor
public class MeetingEntityAdminImplementation implements MeetingEntityAdminService {

    private final UserEntityRepository userEntityRepository;

    private final RoomEntityRepository roomEntityRepository;

    private final MeetingEntityRepository meetingEntityRepository;

    private final CalendarEventService calendarEventService;

    private final MailService mailService;

    private final TaskTrigger taskTrigger;

    @Override
    public ResponseDTO addMeetings(MeetingEntityRequestDTO meetingEntityDTO) {
        try{
            String email= SecurityContextHolder.getContext().getAuthentication().getName();
            System.out.println(email);
            Optional<UserEntity> userEntityOptional = userEntityRepository.findByEmail(email);

            if (userEntityOptional.isPresent()) {
                UserEntity userEntity = userEntityOptional.get();
                Long id = userEntity.getId();
                LocalDateTime startTime = LocalDateTime.parse(meetingEntityDTO.getStartTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
                LocalDateTime endTime = LocalDateTime.parse(meetingEntityDTO.getEndTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));

                Timestamp startTimestamp = Timestamp.valueOf(startTime);
                Timestamp endTimestamp = Timestamp.valueOf(endTime);
                int date=startTimestamp.compareTo(endTimestamp);
                if(date>0){
                    throw new InvalidDataException("Start time is greater than end time");
                }
                MeetingEntity meetingEntity = MeetingEntity.builder()
                        .meetingName(meetingEntityDTO.getMeetingName())
                        .description(meetingEntityDTO.getDescription())
                        .startTime(startTimestamp)
                        .endTime(endTimestamp)
                        .status(MeetingStatusEntity.ACCEPTED)
                        .isDeleted(false)
                        .build();

                RoomEntity roomEntity = roomEntityRepository.findById(meetingEntityDTO.getRoomEntityId())
                        .orElseThrow(() -> new DataNotFoundException("Room Not Found"));
                meetingEntity.setRoomEntity(roomEntity);
                UserEntity host = userEntityRepository.findById(id)
                        .orElseThrow(() -> new DataNotFoundException("User Not found"));
                meetingEntity.setHost(host);

                MeetingEntity savedMeeting = meetingEntityRepository.save(meetingEntity);

                return ResponseDTO.builder()
                        .httpStatus(HttpStatus.OK.getReasonPhrase())
                        .message("Meeting added successfully")
                        .data(savedMeeting)
                        .build();
            }
            else{
                throw new DataNotFoundException("User not found for email: " + email);
            }

        }
        catch (DataNotFoundException ex) {
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.NOT_FOUND.getReasonPhrase())
                    .message("Data Not Found")
                    .build();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                    .message("An error occurred while fetching upcoming meetings")
                    .build();
        }
    }

    @Override
    public ResponseDTO deleteMeetingById(List<Long> ids) {
        try {
            List<MeetingEntity> meetings = meetingEntityRepository.findByIdIn(ids);

            if (meetings.isEmpty()) {
                return ResponseDTO.builder()
                        .httpStatus(HttpStatus.NOT_FOUND.getReasonPhrase())
                        .message("No Meetings Found")
                        .build();
            }
            meetings.forEach(meeting -> {
                meeting.setStatus(MeetingStatusEntity.CANCELLED);
                meeting.setDeleted(true);
            });

            meetingEntityRepository.saveAll(meetings);
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.OK.getReasonPhrase())
                    .message("Meeting Deleted Successfully")
                    .data("DeletedMeeting")
                    .build();
        } catch (DataNotFoundException e) {
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.NOT_FOUND.getReasonPhrase())
                    .message("Meeting Not Found")
                    .build();
        }
        catch (Exception e) {
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                    .message("Error Deleting Meeting")
                    .build();
        }
    }

    @Override
    public ResponseDTO upcomingMeetingsbyhost() {
        try {
            String email=SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity user = userEntityRepository.findByEmail(email)
                    .orElseThrow(() -> new InvalidEmailException(email));

            List<MeetingEntity> hostMeetings = meetingEntityRepository
                    .findByHostIdAndStartTimeAfterOrderByStartTime(user.getId(), Timestamp.valueOf(LocalDateTime.now()));

            List<MeetingEntity> attendeeMeetings = meetingEntityRepository
                    .findByGuestListAndStartTimeAfterOrderByStartTime(user, Timestamp.valueOf(LocalDateTime.now()));

            List<MeetingEntity> allMeetings = Stream.concat(hostMeetings.stream(), attendeeMeetings.stream())
                    .filter(meetingEntity -> MeetingStatusEntity.ACCEPTED.equals(meetingEntity.getStatus()))
                    .filter(meetingEntity -> !meetingEntity.isDeleted())
                    .collect(Collectors.toList());

            List<MeetingEntityActivitiesDTO> upcomingMeetingsDTO = allMeetings.stream()
                    .map(this::convertToActivitiesDTO)
                    .collect(Collectors.toList());

            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.OK.getReasonPhrase())
                    .message("Upcoming Meetings Fetched Successfully")
                    .data(upcomingMeetingsDTO)
                    .build();
        } catch (Exception ex) {
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                    .message("An error occurred while fetching upcoming meetings")
                    .build();
        }
    }

    @Override
    public ResponseDTO requests(LocalDate date) {
        try {
            Timestamp timestamp;

            if (date == null) {
                LocalDateTime now = LocalDateTime.now();
                timestamp = Timestamp.valueOf(now);
            } else {
                timestamp = Timestamp.valueOf(date.atStartOfDay());
            }

            List<MeetingEntity> meetings = meetingEntityRepository.findByStartTimeAfterOrderByStartTime(timestamp);

            if(meetings.isEmpty()){
                throw new DataNotFoundException("Meetings Not Found");
            }
            List<MeetingEntity> pendingMeetings = meetings.stream()
                    .filter(meeting -> MeetingStatusEntity.PENDING.equals(meeting.getStatus()))
                    .filter(meetingEntity -> meetingEntity.isDeleted()==false)
                    .collect(Collectors.toList());

            List<MeetingEntityActivitiesDTO> meetingDTOs = pendingMeetings.stream()
                    .map(this::convertToActivitiesDTO)
                    .collect(Collectors.toList());

            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.OK.getReasonPhrase())
                    .message("Pending Meetings Fetched Successfully")
                    .data(meetingDTOs)
                    .build();
        }
        catch (DataNotFoundException ex) {
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.NOT_FOUND.getReasonPhrase())
                    .message("Meetings Not Found")
                    .build();
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                    .message("Error Fetching Details")
                    .build();
        }
    }


    @Override
    public ResponseDTO upcomingMeetingsbyDate() {
        try {
            Timestamp timestamp;
            LocalDateTime now = LocalDateTime.now();
            timestamp = Timestamp.valueOf(now);

            List<MeetingEntity> upcomingMeetings = meetingEntityRepository.findByStartTimeAfterOrderByStartTime(timestamp);

            if(upcomingMeetings.isEmpty()){
                throw new DataNotFoundException("Meetings Not Found");
            }
            List<MeetingEntityActivitiesDTO> upcomingMeetingsDTO = upcomingMeetings.stream()
                    .filter(meeting -> MeetingStatusEntity.ACCEPTED.equals(meeting.getStatus()))
                    .filter(meetingEntity -> meetingEntity.isDeleted()==false)
                    .map(this::convertToActivitiesDTO)
                    .collect(Collectors.toList());

            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.OK.getReasonPhrase())
                    .message("Upcoming Meetings Fetched Successfully")
                    .data(upcomingMeetingsDTO)
                    .build();
        }catch (DataNotFoundException ex) {
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.NOT_FOUND.getReasonPhrase())
                    .message("Meetings Not Found")
                    .build();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                    .message("An error occurred while fetching upcoming meetings")
                    .build();
        }
    }


    @Override
    public ResponseDTO history(LocalDate startDate, LocalDate endDate) {
        try {
            Timestamp startTimestamp;
            Timestamp endTimestamp;

            if (startDate == null && endDate == null) {
                LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
                LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusNanos(1);

                startTimestamp = Timestamp.valueOf(startOfMonth);
                endTimestamp = Timestamp.valueOf(endOfMonth);
            } else {
                startTimestamp = Timestamp.valueOf(startDate.atStartOfDay());
                endTimestamp = Timestamp.valueOf(endDate.atStartOfDay().plusDays(1).minusNanos(1));
            }

            List<MeetingEntity> meetings = meetingEntityRepository.findByStartDateAndEndDate(startTimestamp, endTimestamp);
            if (meetings.isEmpty()) {
                throw new DataNotFoundException("Meetings Not Found");
            }

            Map<String, Long> categoryCounts = meetings.stream()
                    .filter(meeting -> MeetingStatusEntity.COMPLETED.equals(meeting.getStatus()))
                    .collect(Collectors.groupingBy(
                            meeting -> meeting.getMeetingCategoryEntity().getCategoryName(),
                            Collectors.counting()
                    ));

            categoryCounts.putIfAbsent("Manager Meeting", 0L);
            categoryCounts.putIfAbsent("Team Meeting", 0L);
            categoryCounts.putIfAbsent("Client Meeting", 0L);

            meetings.sort(Comparator.comparing(MeetingEntity::getStartTime).reversed());

            List<MeetingEntityActivitiesDTO> meetingsDTO = meetings.stream()
                    .filter(meeting -> MeetingStatusEntity.COMPLETED.equals(meeting.getStatus()))
                    .map(this::convertToActivitiesDTO)
                    .collect(Collectors.toList());

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("meetings", meetingsDTO);
            responseData.put("categoryCounts", categoryCounts);

            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.OK.getReasonPhrase())
                    .message("Meetings Fetched Successfully")
                    .data(responseData)
                    .build();
        } catch (DataNotFoundException ex) {
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.NOT_FOUND.getReasonPhrase())
                    .message("Meetings Not Found")
                    .build();
        } catch (InvalidDataException e) {
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST.getReasonPhrase())
                    .message("Data Entered is not Valid")
                    .build();
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                    .message("An error occurred while fetching meetings")
                    .build();
        }
    }

    @Override
    public ResponseDTO unblock(Long roomId, LocalDate date) {
        try{
            String email=SecurityContextHolder.getContext().getAuthentication().getName();
            Long id = userEntityRepository.findByEmail(email).orElseThrow(() -> new InvalidEmailException(email)).getId();
            if(!roomEntityRepository.existsById(roomId) || !userEntityRepository.existsById(id)){
                throw new DataNotFoundException("Id Not Found");
            }
            if(date==null){
                date=LocalDate.now();
            }
            List<MeetingEntity> meetings = meetingEntityRepository.findByRoomEntityIdAndHostIdAndStartTimeBetween(
                    roomId,
                    id,
                    date
            );

            List<MeetingEntityActivitiesDTO> meetingDTOs = meetings.stream()
                    .filter(meeting -> MeetingStatusEntity.ACCEPTED.equals(meeting.getStatus()))
                    .filter(meetingEntity -> meetingEntity.isDeleted()==false)
                    .map(this::convertToActivitiesDTO)
                    .collect(Collectors.toList());
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.OK.getReasonPhrase())
                    .message("Meeting Fetched Successfully")
                    .data(meetingDTOs)
                    .build();
        }
        catch (DataNotFoundException ex) {
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.NOT_FOUND.getReasonPhrase())
                    .message("Meetings Not Found")
                    .build();
        }catch(InvalidDataException e){
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST.getReasonPhrase())
                    .message("Data Entered is not Valid")
                    .build();
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                    .message("Error Fetching Details")
                    .build();
        }
    }

    @Override
    public ResponseDTO acceptMeeting(Long meetingId) {
        try{
            MeetingEntity meeting = meetingEntityRepository.findById(meetingId)
                    .orElseThrow(() -> new DataNotFoundException("Meeting not found"));
            meeting.setStatus(MeetingStatusEntity.ACCEPTED);
            meetingEntityRepository.save(meeting);

            mailService.SendMailToMeetingAttenders(meetingId);

            CreateEventDto createEventDto = convertMeetingToCreateEventDto(meeting);
            ResponseEntity<?> calendarResponse = calendarEventService.calendarCreateEvent(createEventDto);

            taskTrigger.scheduleTasksAtSpecifiedTimes(meeting);

            if (calendarResponse.getStatusCode().is2xxSuccessful()) {
                return ResponseDTO.builder()
                        .httpStatus(HttpStatus.OK.getReasonPhrase())
                        .message("Accepted. Calendar event created successfully.")
                        .build();
            } else {
                return ResponseDTO.builder()
                        .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                        .message("Accepted, but failed to create the calendar event.")
                        .build();
            }
        }        catch (Exception e){
            e.printStackTrace();
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                    .message("Error Fetching Details")
                    .build();
        }
    }

    @Override
    public ResponseDTO rejectMeeting(Long meetingId, MeetingEntityRequestDTO meetingEntityDTO) {
        try{
            MeetingEntity meeting = meetingEntityRepository.findById(meetingId)
                    .orElseThrow(() -> new DataNotFoundException("Meeting not found"));
            if(meeting.getStatus().equals(MeetingStatusEntity.PENDING) && meeting.isDeleted()==false){
                meeting.setStatus(MeetingStatusEntity.REJECTED);
                meeting.setReason(meetingEntityDTO.getReason());
            }
            System.out.println(meetingEntityDTO.getReason());
            meetingEntityRepository.save(meeting);

            String reason = meeting.getReason();
            mailService.MeetingCancel(meetingId, reason);

            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.OK.getReasonPhrase())
                    .message("Rejected")
                    .build();
        }   catch (Exception e){
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                    .message("Error Fetching Details")
                    .build();
        }
    }

    @Override
    public ResponseDTO rejectAcceptedMeeting(Long meetingId, MeetingEntityRequestDTO meetingEntityDTO) {
        try {
            MeetingEntity meeting = meetingEntityRepository.findById(meetingId)
                    .orElseThrow(() -> new DataNotFoundException("Meeting not found"));

            if (MeetingStatusEntity.ACCEPTED.equals(meeting.getStatus()) && meeting.isDeleted()==false) {
                meeting.setStatus(MeetingStatusEntity.REJECTED);
                meeting.setReason(meetingEntityDTO.getReason());
                meetingEntityRepository.save(meeting);

                String reason = meeting.getReason();
                mailService.MeetingCancel(meetingId, reason);

                ResponseEntity<?> calendarResponse = deleteEventFromCalendar(meeting);
                if (calendarResponse.getStatusCode().is2xxSuccessful()) {
                    return ResponseDTO.builder()
                            .httpStatus(HttpStatus.OK.getReasonPhrase())
                            .message("Meeting rejected and deleted from Google Calendar.")
                            .build();
                } else {
                    return ResponseDTO.builder()
                            .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                            .message("Meeting rejected, but failed to delete from Google Calendar.")
                            .build();
                }
            } else {
                return ResponseDTO.builder()
                        .httpStatus(HttpStatus.BAD_REQUEST.getReasonPhrase())
                        .message("Meeting is not in ACCEPTED state.")
                        .build();
            }
        } catch (DataNotFoundException e) {
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.NOT_FOUND.getReasonPhrase())
                    .message("Meeting Not Found")
                    .build();
        } catch (Exception e) {
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                    .message("Error Fetching Details")
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
                .startTime(startTime.format(formatter))
                .endTime(endTime.format(formatter))
                .status(meetingEntity.getStatus())
                .roomName(meetingEntity.getRoomEntity().getName())
                .hostName(meetingEntity.getHost().getName())
                .hostEmail(meetingEntity.getHost().getEmail())
                .meetingCategory(meetingEntity.getMeetingCategoryEntity().getCategoryName())
                .build();
    }

    private CreateEventDto convertMeetingToCreateEventDto(MeetingEntity meeting) {
        CreateEventDto createEventDto = new CreateEventDto();
        createEventDto.setMeetingId(meeting.getId());
        createEventDto.setSummary(meeting.getMeetingName());

        createEventDto.setStartTime(new Date(meeting.getStartTime().getTime()));
        createEventDto.setEndTime(new Date(meeting.getEndTime().getTime()));

        createEventDto.setDescription(meeting.getDescription());

        List<String> attendees = meeting.getGuestList().stream()
                .map(UserEntity::getEmail)
                .collect(Collectors.toList());
        createEventDto.setAttendees(attendees);

        createEventDto.setOrganizerEmail(meeting.getHost().getEmail());

        return createEventDto;
    }


}