package com.divum.MeetingRoomBlocker.Service.AdminService;

import com.divum.MeetingRoomBlocker.DTO.MeetingEntityDTO;
import com.divum.MeetingRoomBlocker.DTO.MeetingEntityRequestDTO;
import com.divum.MeetingRoomBlocker.DTO.ResponseDTO;
import com.divum.MeetingRoomBlocker.DTO.RoomEntityDTO;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public interface MeetingEntityAdminService {

    ResponseDTO addMeetings(MeetingEntityRequestDTO meetingEntityDTO);

    ResponseDTO deleteMeetingById(List<Long> ids);

    ResponseDTO upcomingMeetingsbyhost();

    ResponseDTO requests(LocalDate date);

    ResponseDTO upcomingMeetingsbyDate();

    ResponseDTO history(LocalDate startDate, LocalDate endDate);

    ResponseDTO unblock(Long roomId, LocalDate date);

    ResponseDTO acceptMeeting(Long meetingId);

    ResponseDTO rejectMeeting(Long meetingId, MeetingEntityRequestDTO meetingEntityDTO);

    ResponseDTO rejectAcceptedMeeting(Long meetingId, MeetingEntityRequestDTO meetingEntityDTO);
}
