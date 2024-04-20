package com.divum.MeetingRoomBlocker.API.UserAPI;

import com.divum.MeetingRoomBlocker.DTO.ResponseDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/v1/api/user/attendees")
public interface AttendeesUserAPI {

    @GetMapping("internalattendees/")
    public ResponseDTO getInternalAttendees();

}
