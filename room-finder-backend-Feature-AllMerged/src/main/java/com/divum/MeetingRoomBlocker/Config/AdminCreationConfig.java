package com.divum.MeetingRoomBlocker.Config;


import com.divum.MeetingRoomBlocker.Entity.Enum.RoleEntity;
import com.divum.MeetingRoomBlocker.Entity.UserEntity;
import com.divum.MeetingRoomBlocker.Repository.UserEntityRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class AdminCreationConfig {
    private final UserEntityRepository userEntityRepository;
    @PostConstruct
    public void CreateDefaultAdmin(){
        List<UserEntity> userEntityList = userEntityRepository.findByRole(RoleEntity.ADMIN);
        if(userEntityList.isEmpty()){
            UserEntity userEntity = UserEntity.builder()
                    .isDeleted(false)
                    .role(RoleEntity.ADMIN)
                    .name("Admin")
                    .email("abuthakir.ansarali@divum.in")
                    .build();
            userEntityRepository.save(userEntity);
        }
    }
}
