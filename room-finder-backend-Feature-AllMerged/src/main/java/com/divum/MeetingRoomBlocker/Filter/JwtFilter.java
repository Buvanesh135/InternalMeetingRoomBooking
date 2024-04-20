package com.divum.MeetingRoomBlocker.Filter;

import com.divum.MeetingRoomBlocker.DTO.ResponseDTO;
import com.divum.MeetingRoomBlocker.Entity.UserEntity;
import com.divum.MeetingRoomBlocker.Exception.InvalidDataException;
import com.divum.MeetingRoomBlocker.Service.ActivityServices;
import com.divum.MeetingRoomBlocker.Service.JwtServices;
import com.divum.MeetingRoomBlocker.Service.UserServices;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtServices jwtServices;
    private final UserServices userServices;
    private final ObjectMapper objectMapper;
    private final ActivityServices activityServices;
    @Override
    protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHead = request.getHeader("Authorization");
        final String JwtToken;
        if (authHead == null || !authHead.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }
        JwtToken = authHead.substring(7);
        try{
            String userEmail = jwtServices.extractUserName(JwtToken);
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userServices.loadUserByUsername(userEmail);
                String id = jwtServices.extractSessionId(JwtToken);
                if(jwtServices.isTokenValid(JwtToken,userDetails)){
                    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                            userDetails,null,userDetails.getAuthorities()
                    );
                    token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    securityContext.setAuthentication(token);
                    SecurityContextHolder.setContext(securityContext);
                }
            }
            filterChain.doFilter(request,response);
        }catch (UsernameNotFoundException e){
            ErrorWriter(response,e,HttpStatus.NOT_FOUND);
        }catch (SignatureException | MalformedJwtException e){
            ErrorWriter(response,e,HttpStatus.FORBIDDEN);
        }catch (InvalidDataException | NullPointerException e){
            ErrorWriter(response,e,HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            ErrorWriter(response,e,HttpStatus.FORBIDDEN);
        }
    }
    private void ErrorWriter(HttpServletResponse response, Exception e, HttpStatus httpStatus) throws IOException {
        response.setStatus(httpStatus.value());
        ResponseDTO responseDTO = ResponseDTO.builder()
                .data(null)
                .message(e.getMessage())
                .httpStatus(httpStatus.getReasonPhrase())
                .build();
        response.getWriter().write(objectMapper.writeValueAsString(responseDTO));
    }
}