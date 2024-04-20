package com.divum.MeetingRoomBlocker.DTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeddBackSchedulerDTO {
    private String Host;
    private LocalDateTime localDateTime;
}
