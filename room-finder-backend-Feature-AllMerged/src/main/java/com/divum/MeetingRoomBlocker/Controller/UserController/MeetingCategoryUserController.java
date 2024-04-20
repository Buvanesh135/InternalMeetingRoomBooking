package com.divum.MeetingRoomBlocker.Controller.UserController;

import com.divum.MeetingRoomBlocker.API.UserAPI.MeetingCategoryUserAPI;
import com.divum.MeetingRoomBlocker.DTO.ResponseDTO;
import com.divum.MeetingRoomBlocker.Service.UserService.MeetingCategoryEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class MeetingCategoryUserController implements MeetingCategoryUserAPI {

    private final MeetingCategoryEntityService meetingCategoryEntityService;

    @Override
    public ResponseDTO getCategories(){
        return meetingCategoryEntityService.getCategories();
    }

}
