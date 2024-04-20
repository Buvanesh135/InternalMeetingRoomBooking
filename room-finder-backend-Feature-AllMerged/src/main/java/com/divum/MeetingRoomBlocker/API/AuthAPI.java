package com.divum.MeetingRoomBlocker.API;
import com.divum.MeetingRoomBlocker.DTO.LoginDTO;
import com.divum.MeetingRoomBlocker.Service.AuthServices;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/v1/auth")
public interface AuthAPI {
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO, @RequestHeader("User-Agent") String userEnv,HttpServletResponse response);

    @GetMapping("/test")
    public ResponseEntity<?> test(@RequestHeader("User-Agent") String header);

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request,HttpServletResponse response,@RequestHeader("User-Agent") String userEnv);

    @GetMapping("/getAccessToken")
    public ResponseEntity<?> getAccessToken(HttpServletRequest request);


}
