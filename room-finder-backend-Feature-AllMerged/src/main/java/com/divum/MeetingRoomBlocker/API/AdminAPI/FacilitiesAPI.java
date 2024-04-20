package com.divum.MeetingRoomBlocker.API.AdminAPI;
import com.divum.MeetingRoomBlocker.Service.AdminService.FacilitiesServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/v1/api/admin/facilities")
public interface FacilitiesAPI {

    @PostMapping("/add")
    public ResponseEntity<?> addFacility(@RequestParam("facilityIcon")MultipartFile multipartFile,@RequestParam("facilityName") String facilityName);

    @GetMapping("/display")
    public ResponseEntity<?> getAllFacility();
}
