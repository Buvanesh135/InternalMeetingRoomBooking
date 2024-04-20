package com.divum.MeetingRoomBlocker.Implementation;

import com.divum.MeetingRoomBlocker.DTO.MeetingEntityDTO;
import com.divum.MeetingRoomBlocker.DTO.ResponseDTO;
import com.divum.MeetingRoomBlocker.DTO.UserEntityDTO;
import com.divum.MeetingRoomBlocker.Entity.MeetingEntity;
import com.divum.MeetingRoomBlocker.Entity.UserEntity;
import com.divum.MeetingRoomBlocker.Repository.UserEntityRepository;
import com.divum.MeetingRoomBlocker.Service.UserEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserEntityImplementation implements UserEntityService {

    private final UserEntityRepository userRepository;

    @Override
    public ResponseDTO getUserByEmail() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            List<UserEntityDTO> userEntityDTOS = userOptional.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.OK.getReasonPhrase())
                    .message("User details retrieved successfully")
                    .data(userEntityDTOS)
                    .build();
        } else {
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.NOT_FOUND.getReasonPhrase())
                    .message("User not found")
                    .build();
        }
    }

    private UserEntityDTO convertToDTO(UserEntity userEntity) {
        return UserEntityDTO.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .email(userEntity.getEmail())
                .build();
    }
}
