package com.divum.MeetingRoomBlocker.DTO.CalendatEventDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventDto {
    private String summary;
    private String description;
    private String organizerEmail;
    private List<String> attendeesToAdd;
    private List<String> attendeesToRemove;
}
