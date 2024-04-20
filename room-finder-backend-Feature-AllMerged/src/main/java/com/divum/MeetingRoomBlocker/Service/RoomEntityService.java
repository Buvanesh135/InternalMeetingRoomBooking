package com.divum.MeetingRoomBlocker.Service;

import com.divum.MeetingRoomBlocker.DTO.ResponseDTO;
import com.divum.MeetingRoomBlocker.Entity.RoomEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoomEntityService {

    ResponseDTO getAllRooms();

    ResponseDTO getRoomsById(Long id);
}
