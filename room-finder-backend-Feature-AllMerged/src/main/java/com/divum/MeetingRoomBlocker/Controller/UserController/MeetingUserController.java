package com.divum.MeetingRoomBlocker.Controller.UserController;

import com.divum.MeetingRoomBlocker.API.UserAPI.MeetingUserAPI;
import com.divum.MeetingRoomBlocker.DTO.MeetingEntityRequestDTO;
import com.divum.MeetingRoomBlocker.DTO.ResponseDTO;
import com.divum.MeetingRoomBlocker.Entity.Enum.MeetingStatusEntity;
import com.divum.MeetingRoomBlocker.Service.UserService.MeetingEntityUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDateTime;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class MeetingUserController implements MeetingUserAPI {

    private final MeetingEntityUserService meetingEntityUserService;

    //meetingBooing
    @Override
    public ResponseDTO  roomBooking( @RequestBody MeetingEntityRequestDTO meetingEntity){
        return meetingEntityUserService.addMeeting(meetingEntity);
}

    //GetMeeting
  @Override
    public ResponseDTO getMeeting(@PathVariable Long id){
        return  meetingEntityUserService.getMeetingDetails(id);
  }

  //editMeeting
   @Override
    public ResponseDTO editMeeting(@PathVariable Long id,@RequestBody MeetingEntityRequestDTO meetingEntityDTO){
        return  meetingEntityUserService.editMeetingDetails(id,meetingEntityDTO);
   }

   //UserDashBoardApi
   @Override
    public ResponseDTO getUserMeetingDate( @PathVariable Date date){
        return  meetingEntityUserService.getUserMeetingDetails(date);

   }

    //GetCompletedMeeting
    @Override
    public ResponseDTO getCompletedMeeting( @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd")
                                            LocalDateTime date ){
        return  meetingEntityUserService.getCompletedMeeting(date);
    }

    //UpcomingMeeting
    @Override
    public ResponseDTO getUpcomingMeeting(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd")
                                              LocalDateTime date, @PathVariable MeetingStatusEntity status){
        return  meetingEntityUserService.getUpcomingMeetingDetails(date,status);
    }

   //Withdraw and cancel Meeting
    @Override
    public ResponseDTO withdrawMeeting(@PathVariable Long id){
        return meetingEntityUserService.withdrawMeeting(id);
    }


}
