package com.divum.MeetingRoomBlocker.API;

import com.divum.MeetingRoomBlocker.DTO.ResponseDTO;
import com.divum.MeetingRoomBlocker.Service.MeetingEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RequestMapping("/v1/api/bookings")
public interface MeetingEntityAPI {

    @GetMapping("/viewslots/{id}")
    public ResponseDTO viewallslots(@PathVariable Long id, @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date);
}
