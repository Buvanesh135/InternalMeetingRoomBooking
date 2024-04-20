package com.divum.MeetingRoomBlocker.Controller;
import com.divum.MeetingRoomBlocker.API.AuthAPI;
import com.divum.MeetingRoomBlocker.DTO.LoginDTO;
import com.divum.MeetingRoomBlocker.Service.AuthServices;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class AuthController implements AuthAPI {

    private final AuthServices authServices;

    @Override
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO, @RequestHeader("User-Agent") String userEnv,HttpServletResponse response){
        return authServices.loginService(loginDTO,userEnv,response);
    }

    @Override
    public ResponseEntity<?> test(@RequestHeader("User-Agent") String header){
        return new ResponseEntity<>(SecurityContextHolder.getContext().getAuthentication().getName(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> logout(HttpServletRequest request,HttpServletResponse response,@RequestHeader("User-Agent") String userEnv){
        System.out.println("Hello");
        return authServices.logoutService(request, response, userEnv);
    }

    @Override
    public ResponseEntity<?> getAccessToken(HttpServletRequest request){
        return authServices.getAccessToken(request);
    }


}
