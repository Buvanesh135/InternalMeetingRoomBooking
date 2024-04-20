package com.divum.MeetingRoomBlocker.Service;

import com.divum.MeetingRoomBlocker.DTO.ResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface UserEntityService {

    ResponseDTO getUserByEmail();
}
