package com.divum.MeetingRoomBlocker.DTO;

import com.divum.MeetingRoomBlocker.Entity.Enum.MeetingStatusEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeetingEntityActivitiesDTO {
    private Long id;
    private String meetingName;
    private String  meetingCategory;
    private String description;
    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private String hostEmail;
    private MeetingStatusEntity status;
    private String roomName;
    private String hostName;
    private List<InternalUserDTO> guestList;

}
