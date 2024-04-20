package com.divum.MeetingRoomBlocker.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO {

    private boolean isAdmin;
    private String email;
    private String name;
    private String accessToken;
    private String refreshToken;


}
