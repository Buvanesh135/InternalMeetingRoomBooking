package com.divum.MeetingRoomBlocker.Controller;


import com.divum.MeetingRoomBlocker.API.UserEntityAPI;
import com.divum.MeetingRoomBlocker.DTO.ResponseDTO;
import com.divum.MeetingRoomBlocker.Service.UserEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class UserEntityController implements UserEntityAPI {

    private final UserEntityService userEntityService;

    @Override
    public ResponseDTO getUserById() {
        return userEntityService.getUserByEmail();
    }

}
