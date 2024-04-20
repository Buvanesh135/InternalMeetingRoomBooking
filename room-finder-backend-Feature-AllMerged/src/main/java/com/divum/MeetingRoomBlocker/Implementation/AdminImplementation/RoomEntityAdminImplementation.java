package com.divum.MeetingRoomBlocker.Implementation.AdminImplementation;

import com.divum.MeetingRoomBlocker.DTO.ImageUploadResponseDTO;
import com.divum.MeetingRoomBlocker.DTO.ResponseDTO;
import com.divum.MeetingRoomBlocker.DTO.RoomEntityDTO;
import com.divum.MeetingRoomBlocker.Entity.FacilitiesEntity;
import com.divum.MeetingRoomBlocker.Entity.RoomEntity;
import com.divum.MeetingRoomBlocker.Exception.CloudStorageException;
import com.divum.MeetingRoomBlocker.Exception.DataNotFoundException;
import com.divum.MeetingRoomBlocker.Exception.DuplicateItemError;
import com.divum.MeetingRoomBlocker.Exception.InvalidDataException;
import com.divum.MeetingRoomBlocker.Repository.FacilitiesEntityRepository;
import com.divum.MeetingRoomBlocker.Repository.RoomEntityRepository;
import com.divum.MeetingRoomBlocker.Service.AdminService.RoomEntityAdminService;
import com.divum.MeetingRoomBlocker.Service.AwsServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RoomEntityAdminImplementation implements RoomEntityAdminService {

    private final RoomEntityRepository roomEntityRepository;

    private final FacilitiesEntityRepository facilitiesEntityRepository;

    private final AwsServices awsServices;

    @Override
    public ResponseDTO addRoom(RoomEntityDTO roomEntityDTO) {
        Optional<RoomEntity> roomEntityOptional = roomEntityRepository.findByName(roomEntityDTO.getName());
        if(roomEntityOptional.isPresent()){
            throw new DuplicateItemError("Room Name Already Exist");
        }
        List<FacilitiesEntity> facilitiesEntityList = new ArrayList<>();
        System.out.println(roomEntityDTO.getFacilitiesEntityList());
        for(String facilityName : roomEntityDTO.getFacilitiesEntityList()){
            Optional<FacilitiesEntity> facilitiesEntity = facilitiesEntityRepository.findByFacilityName(facilityName);
            if(facilitiesEntity.isEmpty()) throw new DataNotFoundException("Facility Name With"+facilityName+"Not Found");
            if(facilitiesEntityList.contains(facilitiesEntity.get())) throw new DuplicateItemError("Facility Is Repeated");
            facilitiesEntityList.add(facilitiesEntity.get());
        }
        if(roomEntityDTO.getMaxCapacity()< roomEntityDTO.getMinCapacity()) throw new InvalidDataException("The Minimum Capacity Should Be Less Than Or Equal To The Maximum Capacity");
        ImageUploadResponseDTO imageUploadResponseDTO = awsServices.uploadImage(roomEntityDTO.getRoomImage());
        if(imageUploadResponseDTO.isError()) throw new CloudStorageException(imageUploadResponseDTO.getErrorString());
        RoomEntity roomEntity = RoomEntity.builder()
                .imageUrl(imageUploadResponseDTO.getImageUrl())
                .name(roomEntityDTO.getName())
                .isDeleted(false)
                .isAvailable(true)
                .maxCapacity(roomEntityDTO.getMaxCapacity())
                .minCapacity(roomEntityDTO.getMinCapacity())
                .facilitiesEntityList(facilitiesEntityList)
                .build();
        roomEntityRepository.save(roomEntity);
        return ResponseDTO.builder()
                .httpStatus(HttpStatus.CREATED.getReasonPhrase())
                .message("Room Created Successfully")
                .data(null)
                .build();

    }
}

