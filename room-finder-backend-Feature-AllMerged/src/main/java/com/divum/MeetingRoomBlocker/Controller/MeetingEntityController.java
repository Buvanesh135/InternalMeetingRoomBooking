package com.divum.MeetingRoomBlocker.Controller;

import com.divum.MeetingRoomBlocker.API.MeetingEntityAPI;
import com.divum.MeetingRoomBlocker.DTO.ResponseDTO;
import com.divum.MeetingRoomBlocker.Entity.MeetingEntity;
import com.divum.MeetingRoomBlocker.Repository.MeetingEntityRepository;
import com.divum.MeetingRoomBlocker.Service.MeetingEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
@CrossOrigin
public class MeetingEntityController implements MeetingEntityAPI {

    private final MeetingEntityService meetingEntityService;

    @Override
    public ResponseDTO viewallslots(@PathVariable Long id, @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return meetingEntityService.viewslots(id, date);
    }
}
