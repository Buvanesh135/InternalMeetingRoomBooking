package com.divum.MeetingRoomBlocker.Service.UserService;

import com.divum.MeetingRoomBlocker.DTO.ResponseDTO;

import java.sql.Date;
import java.time.LocalDate;

public interface ScheduleUserService {

    ResponseDTO findByYear( int year);

}
