package com.divum.MeetingRoomBlocker.Implementation;

import com.divum.MeetingRoomBlocker.DTO.ResponseDTO;
import com.divum.MeetingRoomBlocker.Entity.RoomEntity;
import com.divum.MeetingRoomBlocker.Repository.RoomEntityRepository;
import com.divum.MeetingRoomBlocker.Service.RoomEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomEntityImplementation implements RoomEntityService {
    private final RoomEntityRepository roomEntityRepository;
    @Override
    public ResponseDTO getAllRooms() {
        List<RoomEntity> rooms = roomEntityRepository.findAll(Sort.by("id").ascending());
        return ResponseDTO.builder()
                .httpStatus(HttpStatus.OK.getReasonPhrase())
                .message("Rooms Fetched Successfully")
                .data(rooms)
                .build();
    }

    @Override
    public ResponseDTO getRoomsById(Long id) {
        Optional<RoomEntity> rooms = roomEntityRepository.findById(id);
        return ResponseDTO.builder()
                .httpStatus(HttpStatus.OK.getReasonPhrase())
                .message("Rooms Fetched Successfully")
                .data(rooms)
                .build();
    }
}
