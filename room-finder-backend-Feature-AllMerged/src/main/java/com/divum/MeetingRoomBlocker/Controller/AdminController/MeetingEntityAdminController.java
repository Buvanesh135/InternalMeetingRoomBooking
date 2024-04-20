package com.divum.MeetingRoomBlocker.Controller.AdminController;


import com.divum.MeetingRoomBlocker.API.AdminAPI.MeetingEntityAdminAPI;
import com.divum.MeetingRoomBlocker.DTO.MeetingEntityRequestDTO;
import com.divum.MeetingRoomBlocker.DTO.ResponseDTO;
import com.divum.MeetingRoomBlocker.Service.AdminService.MeetingEntityAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class MeetingEntityAdminController implements MeetingEntityAdminAPI {

    private final MeetingEntityAdminService meetingEntityAdminService;

    @Override
    public ResponseDTO addMeetings(@RequestBody MeetingEntityRequestDTO meetingEntityDTO){
        return meetingEntityAdminService.addMeetings(meetingEntityDTO);
    }

    @Override
    public ResponseDTO deleteMeetings(@RequestBody List<Long> ids){
        return meetingEntityAdminService.deleteMeetingById(ids);
    }

    @Override
    public ResponseDTO upcomingmeetings(){
        return meetingEntityAdminService.upcomingMeetingsbyhost();
    }

    @Override
    public ResponseDTO requests(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date){
        return meetingEntityAdminService.requests(date);
    }

    @Override
    public ResponseDTO unblock(@RequestParam Long roomId, @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return meetingEntityAdminService.unblock(roomId, date);
    }

    @Override
    public ResponseDTO upcomingmeetingsbyDate(){
        return meetingEntityAdminService.upcomingMeetingsbyDate();
    }

    @Override
    public ResponseDTO history(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                               @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate){
        return meetingEntityAdminService.history(startDate, endDate);
    }

    @Override
    public ResponseDTO acceptmeeting(@PathVariable Long id){
        return meetingEntityAdminService.acceptMeeting(id);
    }

    @Override
    public ResponseDTO rejectmeeting(@PathVariable Long id, @RequestBody MeetingEntityRequestDTO meetingEntityDTO){
        return meetingEntityAdminService.rejectMeeting(id, meetingEntityDTO);
    }

    @Override
    public ResponseDTO rejectacceptedmeeting(@PathVariable Long id, @RequestBody MeetingEntityRequestDTO meetingEntityDTO){
        return meetingEntityAdminService.rejectAcceptedMeeting(id, meetingEntityDTO);
    }

}
