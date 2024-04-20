package com.divum.MeetingRoomBlocker.DTO.CalendatEventDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor

public class CreateEventDto {
    private Long meetingId;
    private String summary;
    private Date startTime;
    private Date endTime;
    private String description;
    private List<String> attendees;
    private String organizerEmail;
}
