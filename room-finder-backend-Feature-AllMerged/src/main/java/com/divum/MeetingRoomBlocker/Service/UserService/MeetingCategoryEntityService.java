package com.divum.MeetingRoomBlocker.Service.UserService;

import com.divum.MeetingRoomBlocker.DTO.ResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface MeetingCategoryEntityService {
    ResponseDTO getCategories();
}
