package com.divum.MeetingRoomBlocker.API.NotificationAPI;

import com.divum.MeetingRoomBlocker.DTO.FeedBackDTO;
import com.divum.MeetingRoomBlocker.Service.FeedBackServices;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@RequestMapping("/v1/feature/feedback")
public interface FeedBackAPI {

    @PostMapping("/savefeedback")
    public ResponseEntity<?> Savefeedback(@RequestBody FeedBackDTO feedBackDTO) throws Exception;

    @GetMapping("/get/feedback/lastfouruser")
    public ResponseEntity<?> GetAllFeedBack() throws Exception;

    @GetMapping("/get/feedback/date/")
    public  ResponseEntity<?> getFeedBackOfDate(@RequestParam("date") String date);

    @PutMapping("/update/seen/{user_id}")
    public ResponseEntity<?> UpdateFeedBackVisible(@PathVariable long user_id);

    @DeleteMapping("/delete/feedback/{user_id}")
    public  ResponseEntity<?> DeleteFeedBackOfUser(@PathVariable long user_id);

}
