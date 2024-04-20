package com.divum.MeetingRoomBlocker.Service;


import com.divum.MeetingRoomBlocker.DTO.ImageUploadResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface AwsServices {

    ImageUploadResponseDTO uploadImage(MultipartFile multipartFile);
}
