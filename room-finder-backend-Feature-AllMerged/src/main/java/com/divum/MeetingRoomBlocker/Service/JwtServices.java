package com.divum.MeetingRoomBlocker.Service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Function;
@Service
public interface JwtServices {
    public boolean isTokenValid(String token, UserDetails userDetails);
    public Claims extractAllDetails(String token);
    public <T> T extractClaims(String token, Function<Claims,T> ClaimsResolver);
    public String getRefreshToken(UserDetails userDetails,String sessionId);
    public String getJwtToken(Map<String,Object> claims, UserDetails userDetails,String sessionId);
    public String getJwtToken(UserDetails userDetails,String sessionId);

    public String extractUserName(String token);
    public String extractSessionId(String token);
}

