package com.divum.MeetingRoomBlocker.Implementation.AdminImplementation;

import com.divum.MeetingRoomBlocker.DTO.FacilityDTO;
import com.divum.MeetingRoomBlocker.DTO.ImageUploadResponseDTO;
import com.divum.MeetingRoomBlocker.DTO.ResponseDTO;
import com.divum.MeetingRoomBlocker.Entity.FacilitiesEntity;
import com.divum.MeetingRoomBlocker.Exception.CloudStorageException;
import com.divum.MeetingRoomBlocker.Exception.DuplicateItemError;
import com.divum.MeetingRoomBlocker.Repository.FacilitiesEntityRepository;
import com.divum.MeetingRoomBlocker.Service.AdminService.FacilitiesServices;
import com.divum.MeetingRoomBlocker.Service.AwsServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FacilitiesServicesImplementation implements FacilitiesServices {

    private final AwsServices awsServices;
    private final FacilitiesEntityRepository facilitiesEntityRepository;
    @Override
    public ResponseEntity<?> addFacility(MultipartFile facilityIcon, String facilityName) {

        Optional<FacilitiesEntity> facilitiesEntityOptional = facilitiesEntityRepository.findByFacilityName(facilityName);
        if(!facilitiesEntityOptional.isEmpty()){
            throw new DuplicateItemError("Facility Name Should Be Unique");
        }
        ImageUploadResponseDTO imageUploadResponseDTO = awsServices.uploadImage(facilityIcon);
        if(imageUploadResponseDTO.isError()){
            throw new CloudStorageException(imageUploadResponseDTO.getErrorString());
        }

        FacilitiesEntity facilitiesEntity = FacilitiesEntity.builder()
                .facilityName(facilityName)
                .iconUrl(imageUploadResponseDTO.getImageUrl())
                .build();
        facilitiesEntityRepository.save(facilitiesEntity);
        ResponseDTO responseDTO = ResponseDTO.builder()
                .httpStatus(HttpStatus.CREATED.getReasonPhrase())
                .message("Facility Created SuccessFully")
                .data(null)
                .build();
        return new ResponseEntity<>(responseDTO,HttpStatus.CREATED);

    }

    @Override
    public ResponseEntity<?> getAllFacilities() {
        List<FacilityDTO> facilitiesEntities = facilitiesEntityRepository.findAllFacilities();
        ResponseDTO responseDTO = ResponseDTO.builder()
                .httpStatus(HttpStatus.OK.getReasonPhrase())
                .message("Facility List")
                .data(facilitiesEntities)
                .build();
        return new ResponseEntity<>(responseDTO,HttpStatus.OK);

    }
}
