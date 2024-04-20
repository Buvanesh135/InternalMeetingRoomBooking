package com.divum.MeetingRoomBlocker.Implementation;

import com.divum.MeetingRoomBlocker.DTO.MeetingEntityActivitiesDTO;
import com.divum.MeetingRoomBlocker.DTO.MeetingEntityDTO;
import com.divum.MeetingRoomBlocker.DTO.ResponseDTO;
import com.divum.MeetingRoomBlocker.Entity.Enum.MeetingStatusEntity;
import com.divum.MeetingRoomBlocker.Entity.MeetingEntity;
import com.divum.MeetingRoomBlocker.Entity.RoomEntity;
import com.divum.MeetingRoomBlocker.Exception.DataNotFoundException;
import com.divum.MeetingRoomBlocker.Exception.InvalidDataException;
import com.divum.MeetingRoomBlocker.Repository.MeetingEntityRepository;
import com.divum.MeetingRoomBlocker.Repository.RoomEntityRepository;
import com.divum.MeetingRoomBlocker.Service.MeetingEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetingEntityImplementation implements MeetingEntityService {

    private final MeetingEntityRepository meetingEntityRepository;

    private final RoomEntityRepository roomEntityRepository;
    @Override
    public ResponseDTO viewslots(long id, LocalDate date) {
        try{
            Optional<RoomEntity> roomEntityOptional = roomEntityRepository.findById(id);

            if (roomEntityOptional.isEmpty() || roomEntityOptional.get().isDeleted()) {
                throw new DataNotFoundException("Room Not Found");
            }
            if(date==null){
                date=LocalDate.now();
            }
            List<MeetingEntity> meetings = meetingEntityRepository.findByRoomEntityIdAndStartTimeBetween(
                    id,
                    date
            );
            if (meetings.isEmpty()) {
                throw new DataNotFoundException("Meetings Not Found");
            }
            List<MeetingEntityActivitiesDTO> meetingDTOs = meetings.stream()
                    .filter(meeting -> MeetingStatusEntity.ACCEPTED.equals(meeting.getStatus()))
                    .filter(meetingEntity -> meetingEntity.isDeleted()==false)
                    .map(this::convertToActivitiesDTO)
                    .collect(Collectors.toList());
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.OK.getReasonPhrase())
                    .message("Meeting Fetched Successfully")
                    .data(meetingDTOs)
                    .build();
        }
        catch(DataNotFoundException e){
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.NOT_FOUND.getReasonPhrase())
                    .message("Room or Meetings Not Found")
                    .build();
        }
        catch(InvalidDataException e){
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST.getReasonPhrase())
                    .message("Data Entered is not Valid")
                    .build();
        }
        catch (Exception e){
            return ResponseDTO.builder()
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                    .message("Error Fetching Details")
                    .build();
        }
    }
    private MeetingEntityActivitiesDTO convertToActivitiesDTO(MeetingEntity meetingEntity) {
        LocalDateTime startTime = meetingEntity.getStartTime().toLocalDateTime();
        LocalDateTime endTime = meetingEntity.getEndTime().toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        return MeetingEntityActivitiesDTO.builder()
                .id(meetingEntity.getId())
                .meetingName(meetingEntity.getMeetingName())
                .description(meetingEntity.getDescription())
                .startDate(startTime.toLocalDate().toString())
                .endDate(endTime.toLocalDate().toString())
                .startTime(startTime.format(formatter))
                .endTime(endTime.format(formatter))
                .status(meetingEntity.getStatus())
                .roomName(meetingEntity.getRoomEntity().getName())
                .hostName(meetingEntity.getHost().getName())
                .hostEmail(meetingEntity.getHost().getEmail())
                .meetingCategory(meetingEntity.getMeetingCategoryEntity().getCategoryName())
                .build();
    }
}