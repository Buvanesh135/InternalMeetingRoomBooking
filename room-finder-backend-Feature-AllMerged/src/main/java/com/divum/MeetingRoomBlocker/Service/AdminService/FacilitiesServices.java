package com.divum.MeetingRoomBlocker.Service.AdminService;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FacilitiesServices {

    ResponseEntity<?> addFacility(MultipartFile facilityIcon,String facilityName);

    ResponseEntity<?> getAllFacilities();

}
