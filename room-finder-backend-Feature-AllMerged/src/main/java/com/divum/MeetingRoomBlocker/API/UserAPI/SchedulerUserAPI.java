package com.divum.MeetingRoomBlocker.API.UserAPI;


import com.divum.MeetingRoomBlocker.DTO.ResponseDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@RequestMapping("/v1/api/user/schedule/")
public interface SchedulerUserAPI {

    @GetMapping("meetingsbyyear/{year}")
    public ResponseDTO yearandmonthmeetings(@PathVariable("year") int year);

}
