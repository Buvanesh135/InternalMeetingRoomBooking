package com.divum.MeetingRoomBlocker.Service.UserService;

import com.divum.MeetingRoomBlocker.DTO.ResponseDTO;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public interface UserEntityUserService {
    ResponseDTO getInternalUsers();


}
