package com.divum.MeetingRoomBlocker.DTO;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.A;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class CalenderEventDTO {
    @JsonIgnore
    private Long id;
    private String start;
    private String end;
    private String title;
    private String hostName;

}
