package com.divum.MeetingRoomBlocker.DTO;

import com.divum.MeetingRoomBlocker.Entity.Enum.RoleEntity;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntityDTO {

    private Long id;

    private String name;

    private String email;

}
