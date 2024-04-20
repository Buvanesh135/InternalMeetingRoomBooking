package com.divum.MeetingRoomBlocker.Implementation;

import com.divum.MeetingRoomBlocker.Entity.UserActivityEntity;
import com.divum.MeetingRoomBlocker.Entity.UserEntity;
import com.divum.MeetingRoomBlocker.Exception.InvalidDataException;
import com.divum.MeetingRoomBlocker.Repository.UserActivityEntityRepository;
import com.divum.MeetingRoomBlocker.Service.ActivityServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActivityServicesImplementation implements ActivityServices {

    private final UserActivityEntityRepository userActivityEntityRepository;
    @Override
    public boolean isValidData(UserEntity userEntity, String sessionId, String userEnv,String id) {
        Optional<UserActivityEntity> userActivityEntityOptional = userActivityEntityRepository.findBySessionIdAndUserEnvAndId(sessionId,userEnv,Long.parseLong(id));
        if(userActivityEntityOptional.isEmpty()) throw new InvalidDataException("Data Security Error");
        UserActivityEntity userActivityEntity = userActivityEntityOptional.get();
        return userActivityEntity.getUserEntity().getEmail().equals(userEntity.getEmail()) && userActivityEntity.getLogoutTime() == null;

    }
}
