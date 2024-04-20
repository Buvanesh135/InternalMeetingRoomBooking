package com.divum.MeetingRoomBlocker.DTO;

import com.divum.MeetingRoomBlocker.Entity.Enum.MeetingStatusEntity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.type.DateTime;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;


import java.sql.Timestamp;

import java.time.LocalDateTime;
import java.util.List;
@Data
public class MeetingEntityRequestDTO {
    private String meetingName;
    private String  meetingCategory;
    private List<String> userEntityList;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "UTC")
    private String startTime;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "UTC")
    private String endTime;

    @JsonIgnore
    private MeetingStatusEntity status;
    private Long roomEntityId;

    private String Reason;

}
