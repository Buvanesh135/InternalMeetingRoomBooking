package com.divum.MeetingRoomBlocker.API.AdminAPI;


import com.divum.MeetingRoomBlocker.DTO.MeetingEntityRequestDTO;
import com.divum.MeetingRoomBlocker.DTO.ResponseDTO;
import com.divum.MeetingRoomBlocker.Service.AdminService.MeetingEntityAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequestMapping("/v1/api/admin/meetings")
public interface MeetingEntityAdminAPI {

    @PostMapping("/add")
    public ResponseDTO addMeetings(@RequestBody MeetingEntityRequestDTO meetingEntityDTO);

    @PutMapping("/delete/{id}")
    public ResponseDTO deleteMeetings(@RequestBody List<Long> ids);

    @GetMapping("/upcomingmeetingsbyhost")
    public ResponseDTO upcomingmeetings();

    @GetMapping("/requests")
    public ResponseDTO requests(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date);

    @GetMapping("/unblock")
    public ResponseDTO unblock(@RequestParam Long roomId, @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date);

    @GetMapping("/upcomingmeetings")
    public ResponseDTO upcomingmeetingsbyDate();

    @GetMapping("/history")
    public ResponseDTO history(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                               @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate);
    @PutMapping("/accept/{id}")
    public ResponseDTO acceptmeeting(@PathVariable Long id);

    @PutMapping("/reject/{id}")
    public ResponseDTO rejectmeeting(@PathVariable Long id, @RequestBody MeetingEntityRequestDTO meetingEntityDTO);

    @PutMapping("/rejectaccepted/{id}")
    public ResponseDTO rejectacceptedmeeting(@PathVariable Long id, @RequestBody MeetingEntityRequestDTO meetingEntityDTO);

}
