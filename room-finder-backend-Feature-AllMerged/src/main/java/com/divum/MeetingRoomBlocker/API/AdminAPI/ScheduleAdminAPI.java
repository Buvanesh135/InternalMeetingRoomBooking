package com.divum.MeetingRoomBlocker.API.AdminAPI;


import com.divum.MeetingRoomBlocker.DTO.ResponseDTO;
import com.divum.MeetingRoomBlocker.Service.AdminService.ScheduleAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v1/api/admin/meetings")
public interface ScheduleAdminAPI {

    @GetMapping("/meetingsbyyear/{year}")
    public ResponseDTO yearandmonthmeetings(@PathVariable("year")int year);

}
