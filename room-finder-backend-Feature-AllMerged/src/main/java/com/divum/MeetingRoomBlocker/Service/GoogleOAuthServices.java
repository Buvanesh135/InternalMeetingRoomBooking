package com.divum.MeetingRoomBlocker.Service;


import com.divum.MeetingRoomBlocker.DTO.OAuthDetailsDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public interface  GoogleOAuthServices {

    String extractEmail(String accessToken);
    boolean validateUser(String accessToken, UserDetails userDetails);
    OAuthDetailsDTO extractUser(String accessToken);

}