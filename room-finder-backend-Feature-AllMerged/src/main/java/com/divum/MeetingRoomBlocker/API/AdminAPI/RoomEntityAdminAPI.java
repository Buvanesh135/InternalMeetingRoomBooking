package com.divum.MeetingRoomBlocker.API.AdminAPI;

import com.divum.MeetingRoomBlocker.DTO.ResponseDTO;
import com.divum.MeetingRoomBlocker.DTO.RoomEntityDTO;
import com.divum.MeetingRoomBlocker.Service.AdminService.RoomEntityAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v1/api/admin/rooms/")
public interface RoomEntityAdminAPI {

    @PostMapping("/add")
    public ResponseDTO addRoom(@ModelAttribute  RoomEntityDTO roomEntityDTO) ;

}
