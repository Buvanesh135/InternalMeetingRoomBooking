package com.divum.MeetingRoomBlocker.API;

import com.divum.MeetingRoomBlocker.DTO.ResponseDTO;
import com.divum.MeetingRoomBlocker.Service.RoomEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v1/api/rooms")
public interface RoomEntityAPI {

    @GetMapping("/display")
    public ResponseDTO getAllRooms();

    @GetMapping("/displaybyId/{id}")
    public ResponseDTO getRoomsbyId(@PathVariable("id") Long id);
}
