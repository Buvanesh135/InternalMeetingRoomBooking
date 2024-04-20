package com.divum.MeetingRoomBlocker.Service.AdminService;

import com.divum.MeetingRoomBlocker.DTO.ResponseDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public interface ScheduleAdminService {

    ResponseDTO findByYear(int year);

}
