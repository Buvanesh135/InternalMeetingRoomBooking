package com.divum.MeetingRoomBlocker.Implementation.UserImplementation;

import com.divum.MeetingRoomBlocker.DTO.InternalUserDTO;
import com.divum.MeetingRoomBlocker.DTO.ResponseDTO;
import com.divum.MeetingRoomBlocker.Entity.UserEntity;
import com.divum.MeetingRoomBlocker.Exception.DataNotFoundException;
import com.divum.MeetingRoomBlocker.Repository.UserEntityRepository;
import com.divum.MeetingRoomBlocker.Service.UserService.UserEntityUserService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Builder
@RequiredArgsConstructor
public class UserEntityUserImplementation implements UserEntityUserService {
     private final UserEntityRepository userEntityRepository;
    @Override
    public ResponseDTO getInternalUsers() {
       try {
           List<UserEntity> internalUsers = userEntityRepository.findAll();

           if (internalUsers.isEmpty()) {
               throw new DataNotFoundException("Meetings not found");
           }
           List<InternalUserDTO> internalUsersList = internalUsers.stream()
                   .map(this::covertToUserList)
                   .collect(Collectors.toList());

           return ResponseDTO.builder()
                   .httpStatus(HttpStatus.OK.getReasonPhrase())
                   .message("Internal Attendees")
                   .data(internalUsersList)
                   .build();
       }catch(DataNotFoundException e){
           return  ResponseDTO.builder()
                   .httpStatus(HttpStatus.NOT_FOUND.getReasonPhrase())
                   .message("Internal Attendee not found")
                   .build();
       } catch (Exception ex) {
           return ResponseDTO.builder()
                   .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                   .message("An error occurred while fetching Internal Attendee")
                   .build();
       }
    }
    private InternalUserDTO covertToUserList(UserEntity userEntity) {
        return InternalUserDTO.builder()
                .email(userEntity.getEmail())
                .name(userEntity.getName())
                .build();

    }


}
