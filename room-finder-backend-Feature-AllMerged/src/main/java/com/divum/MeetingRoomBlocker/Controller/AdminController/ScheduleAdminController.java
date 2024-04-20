package com.divum.MeetingRoomBlocker.Controller.AdminController;


import com.divum.MeetingRoomBlocker.API.AdminAPI.ScheduleAdminAPI;
import com.divum.MeetingRoomBlocker.DTO.ResponseDTO;
import com.divum.MeetingRoomBlocker.Service.AdminService.ScheduleAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class ScheduleAdminController implements ScheduleAdminAPI {

    private final ScheduleAdminService scheduleAdminService;

    @Override
    public ResponseDTO yearandmonthmeetings(@PathVariable("year")int year){
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
        return scheduleAdminService.findByYear(year);
    }

}
