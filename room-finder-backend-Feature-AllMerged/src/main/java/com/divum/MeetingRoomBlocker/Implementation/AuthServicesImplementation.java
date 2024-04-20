package com.divum.MeetingRoomBlocker.Implementation;
import com.divum.MeetingRoomBlocker.DTO.*;
import com.divum.MeetingRoomBlocker.Entity.Enum.RoleEntity;
import com.divum.MeetingRoomBlocker.Entity.UserActivityEntity;
import com.divum.MeetingRoomBlocker.Entity.UserEntity;
import com.divum.MeetingRoomBlocker.Exception.InvalidDataException;
import com.divum.MeetingRoomBlocker.Exception.InvalidEmailException;
import com.divum.MeetingRoomBlocker.Repository.UserActivityEntityRepository;
import com.divum.MeetingRoomBlocker.Repository.UserEntityRepository;
import com.divum.MeetingRoomBlocker.Service.AuthServices;
import com.divum.MeetingRoomBlocker.Service.GoogleOAuthServices;
import com.divum.MeetingRoomBlocker.Service.JwtServices;
import com.divum.MeetingRoomBlocker.Util.SessionIdGenerator;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class AuthServicesImplementation implements AuthServices {
    private final UserEntityRepository userEntityRepository;
    private final JwtServices jwtServices;
    private final GoogleOAuthServices googleOAuthServices;
    private final UserActivityEntityRepository userActivityEntityRepository;
    private final SessionIdGenerator sessionIdGenerator;
    @Override
    public ResponseEntity<?> loginService(LoginDTO loginDTO, String userEnv, HttpServletResponse response) {
        OAuthDetailsDTO oAuthDetailsDTO = googleOAuthServices.extractUser(loginDTO.getAccessToken());
        String email = oAuthDetailsDTO.getEmail();
        if (!isValidDomain(email)) throw new InvalidEmailException("The Mail Id Does Not Belongs to This Organization");
        UserEntity userEntity;
        Optional<UserEntity> userEntityOptional = userEntityRepository.findByEmail(email);
        if (userEntityOptional.isEmpty()) {
            userEntity = UserEntity.builder()
                    .name(oAuthDetailsDTO.getName())
                    .role(RoleEntity.EMPLOYEE)
                    .email(oAuthDetailsDTO.getEmail())
                    .accessToken(loginDTO.getAccessToken())
                    .refreshToken(loginDTO.getRefreshToken())
                    .isDeleted(false)
                    .build();
            userEntityRepository.save(userEntity);
        } else {
            userEntity = userEntityOptional.get();
            userEntity.setAccessToken(loginDTO.getAccessToken());
            userEntity.setRefreshToken(loginDTO.getRefreshToken());
            userEntityRepository.save(userEntity);
        }
        UserActivityEntity userActivityEntity = UserActivityEntity.builder()
                .sessionId(sessionIdGenerator.getRandomId())
                .userEntity(userEntity)
                .loginTime(new Timestamp(System.currentTimeMillis()))
                .userEnv(userEnv)
                .build();
        userActivityEntityRepository.save(userActivityEntity);
        Cookie cookie = new Cookie("_id",userActivityEntity.getSessionId());
        cookie.setPath("/");
        response.addCookie(cookie);
        userActivityEntity = userActivityEntityRepository.save(userActivityEntity);
        String accessToken = jwtServices.getJwtToken(userEntity, userActivityEntity.getId().toString());
        String refreshToken = jwtServices.getRefreshToken(userEntity, userActivityEntity.getId().toString());
        UserLoginDTO userLoginDTO = UserLoginDTO.builder()
                .isAdmin(userEntity.getRole() == RoleEntity.ADMIN)
                .name(userEntity.getName())
                .email(userEntity.getEmail())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        ResponseDTO responseDTO = ResponseDTO.builder()
                .httpStatus(HttpStatus.OK.getReasonPhrase())
                .message("Login Successful")
                .data(userLoginDTO)
                .build();
        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }
    @Override
    public ResponseEntity<?> logoutService(HttpServletRequest request, HttpServletResponse response, String userEnv) {
        try {
            final String authHead = request.getHeader("Authorization");
            String JwtToken;
            if (authHead == null || !authHead.startsWith("Bearer")) {
                throw new InvalidDataException("Access Not Found");
            }
            JwtToken = authHead.substring(7);
            String id = jwtServices.extractSessionId(JwtToken);
            String sessionId = userActivityEntityRepository.findById(Long.parseLong(id)).get().getSessionId();
            Optional<UserActivityEntity> userActivityEntityOptional = userActivityEntityRepository.findBySessionIdAndUserEnv(sessionId, userEnv);
            if (userActivityEntityOptional.isEmpty()) throw new InvalidDataException("Data Security Error");
            UserActivityEntity userActivityEntity = userActivityEntityOptional.get();
            if (userActivityEntity.getLogoutTime() != null) throw new InvalidDataException("Data Security Error");
            userActivityEntity.setLogoutTime(new Timestamp(System.currentTimeMillis()));
            userActivityEntityRepository.save(userActivityEntity);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            throw new InvalidDataException("Invalid Access Token");
        }
    }
    @Override
    public ResponseEntity<?> getAccessToken(HttpServletRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<UserEntity> userEntityOptional = userEntityRepository.findByEmail(email);
        if (userEntityOptional.isEmpty()) {
            throw new InvalidEmailException("The Email Id Not Found");
        }
        SecurityContext securityContext=SecurityContextHolder.getContext();
        Authentication authentication= securityContext.getAuthentication();
        authentication.getPrincipal();
        String userEnv = request.getHeader("User-Agent");
        final String authHead = request.getHeader("Authorization");
        String JwtToken;
        if (authHead == null || !authHead.startsWith("Bearer")) {
            throw new InvalidDataException("Access Not Found");
        }
        JwtToken = authHead.substring(7);
        String id = jwtServices.extractSessionId(JwtToken);
        String sessionId = userActivityEntityRepository.findById(Long.parseLong(id)).get().getSessionId();
        Optional<UserActivityEntity> userActivityEntityOptional = userActivityEntityRepository.findBySessionIdAndUserEnv(sessionId, userEnv);
        if (userActivityEntityOptional.isEmpty()) throw new InvalidDataException("Data Security Error");
        TokenDTO tokenDTO = TokenDTO.builder()
                .accessToken(jwtServices.getJwtToken(userEntityOptional.get(), String.valueOf(userActivityEntityOptional.get().getId())))
                .refreshToken(jwtServices.getRefreshToken(userEntityOptional.get(), String.valueOf(userActivityEntityOptional.get().getId())))
                .build();
        ResponseDTO responseDTO = ResponseDTO.builder()
                .data(tokenDTO)
                .message("Token Generated Successfully")
                .httpStatus(HttpStatus.OK.getReasonPhrase())
                .build();
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    private boolean isValidDomain(String email) {
        return email.split("@")[1].equals("divum.in") || email.split("@")[1].equals("param.in");
    }
}
