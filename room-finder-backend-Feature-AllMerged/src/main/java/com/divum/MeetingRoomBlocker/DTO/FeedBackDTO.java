package com.divum.MeetingRoomBlocker.DTO;


import com.divum.MeetingRoomBlocker.Entity.MeetingEntity;
import com.divum.MeetingRoomBlocker.Entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FeedBackDTO {
    private String Feedback;
    private Integer Rating;
    private long UserId;
    private long MeetingId;
}
