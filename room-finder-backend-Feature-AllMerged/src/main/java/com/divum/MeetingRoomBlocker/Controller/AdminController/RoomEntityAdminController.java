package com.divum.MeetingRoomBlocker.Controller.AdminController;
import com.divum.MeetingRoomBlocker.API.AdminAPI.RoomEntityAdminAPI;
import com.divum.MeetingRoomBlocker.DTO.ResponseDTO;
import com.divum.MeetingRoomBlocker.DTO.RoomEntityDTO;
import com.divum.MeetingRoomBlocker.Service.AdminService.RoomEntityAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class RoomEntityAdminController implements RoomEntityAdminAPI {

    public final RoomEntityAdminService roomEntityAdminService;

    @Override
    public ResponseDTO addRoom(@ModelAttribute  RoomEntityDTO roomEntityDTO) {
        return roomEntityAdminService.addRoom(roomEntityDTO);
    }
}
