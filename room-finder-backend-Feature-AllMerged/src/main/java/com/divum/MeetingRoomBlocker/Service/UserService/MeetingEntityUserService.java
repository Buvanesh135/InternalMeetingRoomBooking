package com.divum.MeetingRoomBlocker.Service.UserService;

import com.divum.MeetingRoomBlocker.DTO.MeetingEntityDTO;
import com.divum.MeetingRoomBlocker.DTO.MeetingEntityRequestDTO;
import com.divum.MeetingRoomBlocker.DTO.ResponseDTO;
import com.divum.MeetingRoomBlocker.Entity.Enum.MeetingStatusEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public interface MeetingEntityUserService {

    ResponseDTO addMeeting(MeetingEntityRequestDTO meetingEntity);
    ResponseDTO getMeetingDetails(Long id);
    ResponseDTO editMeetingDetails(Long id, MeetingEntityRequestDTO meetingEntityDTO);
    //UserDashBoardApi
    ResponseDTO getUserMeetingDetails( Date date);
    //UpcomingAndPendingMeeting
    ResponseDTO getUpcomingMeetingDetails(LocalDateTime date, MeetingStatusEntity status);
    //GetHostCompletedMeeting
    ResponseDTO getCompletedMeeting( LocalDateTime date);
    ResponseDTO withdrawMeeting(Long id);



}
