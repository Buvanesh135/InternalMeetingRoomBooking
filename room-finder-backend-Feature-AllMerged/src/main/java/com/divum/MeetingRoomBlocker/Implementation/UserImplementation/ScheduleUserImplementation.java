package com.divum.MeetingRoomBlocker.Implementation.UserImplementation;

import com.divum.MeetingRoomBlocker.DTO.CalenderEventDTO;
import com.divum.MeetingRoomBlocker.DTO.MeetingEntityDTO;
import com.divum.MeetingRoomBlocker.DTO.ResponseDTO;
import com.divum.MeetingRoomBlocker.Entity.Enum.MeetingStatusEntity;
import com.divum.MeetingRoomBlocker.Entity.MeetingEntity;
import com.divum.MeetingRoomBlocker.Entity.UserEntity;
import com.divum.MeetingRoomBlocker.Exception.DataNotFoundException;
import com.divum.MeetingRoomBlocker.Exception.InvalidEmailException;
import com.divum.MeetingRoomBlocker.Repository.MeetingEntityRepository;
import com.divum.MeetingRoomBlocker.Repository.UserEntityRepository;
import com.divum.MeetingRoomBlocker.Service.UserService.ScheduleUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class ScheduleUserImplementation implements ScheduleUserService {

    private final MeetingEntityRepository meetingEntityRepository;
    private final UserEntityRepository userEntityRepository;
    @Override
    public ResponseDTO findByYear(int year) {
        try{
            String email= SecurityContextHolder.getContext().getAuthentication().getName();
            Long userId=userEntityRepository.findByEmail(email).orElseThrow(()-> new InvalidEmailException(email)).getId();
            List<MeetingEntity> meetings = meetingEntityRepository.findByYearByUser(userId,year);
            if(meetings.isEmpty()){
                throw new DataNotFoundException("Meetings not found");
            }
            List<MeetingEntityDTO> meetingEntityDTOs = meetings.stream()
                    .filter(meeting -> MeetingStatusEntity.ACCEPTED.equals(meeting.getStatus()))
                    .filter(meetingEntity -> meetingEntity.isDeleted()==false)
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            List<CalenderEventDTO> calenderEvents = meetingEntityDTOs.stream()
                    .map(this::convertToCalenderEventDTO)
                    .collect(Collectors.toList());
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.OK.getReasonPhrase())
                    .message("Meeting Fetched Successfully")
                    .data(calenderEvents)
                    .build();
        }catch(InvalidEmailException e){
            return  ResponseDTO.builder()
                    .httpStatus(HttpStatus.NOT_FOUND.getReasonPhrase())
                    .message("Email  not found")
                    .build();
        } catch(DataNotFoundException e){
            return  ResponseDTO.builder()
                    .httpStatus(HttpStatus.NOT_FOUND.getReasonPhrase())
                    .message("Meeting not found")
                    .build();
        } catch (Exception e){
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                    .message("Error Fetching Details")
                    .build();
        }
    }

    private CalenderEventDTO convertToCalenderEventDTO(MeetingEntityDTO meetingEntityDTO) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("E MMM dd yyyy HH:mm:ss 'GMT'Z", Locale.ENGLISH);

        String start = dateFormat.format(meetingEntityDTO.getStartTime());
        String end = dateFormat.format(meetingEntityDTO.getEndTime());

        String hostName = getHostNameFromUserId(meetingEntityDTO.getHostId());

        return CalenderEventDTO.builder()
                // .id(meetingEntityDTO.getId())
                .start(start)
                .end(end)
                .title(meetingEntityDTO.getMeetingName())
                .hostName(hostName)
                .build();
    }


    public String getHostNameFromUserId(Long userId) {
        Optional<UserEntity> userEntityOptional = userEntityRepository.findUserNameById(userId);

        return userEntityOptional.map(UserEntity::getName).orElse("Unknown Host");
    }

    private MeetingEntityDTO convertToDTO(MeetingEntity meetingEntity) {
        return MeetingEntityDTO.builder()
                // .id(meetingEntity.getId())
                .meetingName(meetingEntity.getMeetingName())
                .description(meetingEntity.getDescription())
                .startTime(meetingEntity.getStartTime())
                .endTime(meetingEntity.getEndTime())
                .status(meetingEntity.getStatus())
                .roomEntityId(meetingEntity.getRoomEntity().getId())
                .hostId(meetingEntity.getHost().getId())
                .build();
    }
}
