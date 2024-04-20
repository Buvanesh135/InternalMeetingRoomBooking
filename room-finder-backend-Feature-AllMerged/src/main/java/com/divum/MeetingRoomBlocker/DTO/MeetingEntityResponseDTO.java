package com.divum.MeetingRoomBlocker.DTO;

import com.divum.MeetingRoomBlocker.Entity.Enum.MeetingStatusEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeetingEntityResponseDTO {
    private String meetingName;
    private String  meetingCategory;
    private String description;
    private String startTime;
    private String  endTime;
    private String startDate;
    private String endDate;
    private MeetingStatusEntity status;
    private String roomName;
    private String hostName;

}
