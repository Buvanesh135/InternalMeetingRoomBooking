package com.divum.MeetingRoomBlocker.Controller.UserController;


import com.divum.MeetingRoomBlocker.API.UserAPI.SchedulerUserAPI;
import com.divum.MeetingRoomBlocker.DTO.ResponseDTO;
import com.divum.MeetingRoomBlocker.Service.UserService.ScheduleUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController()
@CrossOrigin
public class SchedulerUserController implements SchedulerUserAPI {

    private final ScheduleUserService scheduleUserService;

    @Override
    public ResponseDTO yearandmonthmeetings(@PathVariable("year") int year){
        return scheduleUserService.findByYear(year);
    }

}
