package com.divum.MeetingRoomBlocker.Controller.AdminController;
import com.divum.MeetingRoomBlocker.API.AdminAPI.FacilitiesAPI;
import com.divum.MeetingRoomBlocker.Service.AdminService.FacilitiesServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class FacilitiesController implements FacilitiesAPI {

    private final FacilitiesServices facilitiesServices;

    @Override
    public ResponseEntity<?> addFacility(@RequestParam("facilityIcon")MultipartFile multipartFile,@RequestParam("facilityName") String facilityName){
        return facilitiesServices.addFacility(multipartFile,facilityName);
    }

    @Override
    public ResponseEntity<?> getAllFacility(){
        return facilitiesServices.getAllFacilities();
    }
}
