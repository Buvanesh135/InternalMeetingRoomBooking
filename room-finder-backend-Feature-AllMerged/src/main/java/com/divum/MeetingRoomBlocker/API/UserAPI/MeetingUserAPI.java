package com.divum.MeetingRoomBlocker.API.UserAPI;

import com.divum.MeetingRoomBlocker.DTO.MeetingEntityRequestDTO;
import com.divum.MeetingRoomBlocker.DTO.ResponseDTO;
import com.divum.MeetingRoomBlocker.Entity.Enum.MeetingStatusEntity;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDateTime;

@RequestMapping("/v1/api/user/meeting")
public interface MeetingUserAPI {


    @PostMapping("bookmeeting/")
    public ResponseDTO  roomBooking( @RequestBody MeetingEntityRequestDTO meetingEntity);

  @GetMapping("getmeeting/{id}")
    public ResponseDTO getMeeting(@PathVariable Long id);

   @PutMapping("editmeeting/{id}")
    public ResponseDTO editMeeting(@PathVariable Long id,@RequestBody MeetingEntityRequestDTO meetingEntityDTO);

   @GetMapping("getuserdashboardmeeting/date/{date}")
    public ResponseDTO getUserMeetingDate( @PathVariable Date date);

    @GetMapping("get/completedmeeting")
    public ResponseDTO getCompletedMeeting( @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime date);

    @GetMapping("get/upcomingmeeting/{status}")
    public ResponseDTO getUpcomingMeeting(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd")
                                              LocalDateTime date, @PathVariable MeetingStatusEntity status);

    @PutMapping("withdrawmeeting/{id}")
    public ResponseDTO withdrawMeeting(@PathVariable Long id);


}
