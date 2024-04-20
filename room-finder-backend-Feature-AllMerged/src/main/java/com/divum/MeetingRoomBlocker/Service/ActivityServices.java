package com.divum.MeetingRoomBlocker.Service;

import com.divum.MeetingRoomBlocker.Entity.UserEntity;
import org.springframework.stereotype.Service;

@Service
public interface ActivityServices {
    public boolean isValidData(UserEntity userEntity,String sessionId,String userEnv,String id);
}
