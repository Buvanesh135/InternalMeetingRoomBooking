package com.divum.MeetingRoomBlocker.Controller.UserController;

import com.divum.MeetingRoomBlocker.API.UserAPI.AttendeesUserAPI;
import com.divum.MeetingRoomBlocker.DTO.ResponseDTO;
import com.divum.MeetingRoomBlocker.Service.UserService.UserEntityUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@CrossOrigin
public class AttendeesUserController implements AttendeesUserAPI {

    private final UserEntityUserService userEntityUserService;

    @Override
    public ResponseDTO getInternalAttendees(){
        return userEntityUserService.getInternalUsers();
    }

}
