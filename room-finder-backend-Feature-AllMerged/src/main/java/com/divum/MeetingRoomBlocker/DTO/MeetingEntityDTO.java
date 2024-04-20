package com.divum.MeetingRoomBlocker.DTO;

import com.divum.MeetingRoomBlocker.Entity.Enum.MeetingStatusEntity;
import com.divum.MeetingRoomBlocker.Entity.UserEntity;
import lombok.*;
import lombok.Data;


import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeetingEntityDTO {


    private String meetingName;
    private String  meetingCategory;
    private List<UserEntity> userEntityList;
    private String description;
    private Timestamp startTime;
    private Timestamp endTime;
    private MeetingStatusEntity status;
    private Long roomEntityId;
    private Long hostId;

}
