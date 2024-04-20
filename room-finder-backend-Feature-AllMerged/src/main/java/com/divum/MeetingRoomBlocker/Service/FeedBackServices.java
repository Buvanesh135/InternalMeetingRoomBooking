package com.divum.MeetingRoomBlocker.Service;

import com.divum.MeetingRoomBlocker.DTO.FeedBackDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public interface FeedBackServices {
    public ResponseEntity SaveFeedBackOfUser(FeedBackDTO feedBackDTO) throws Exception;
    public ResponseEntity GetFeedBackOfUser() throws Exception;
    public ResponseEntity<?> getFeedBackOnDate(Timestamp timestamp);
    public ResponseEntity<?> UpdateFeedBackVisible(long userId);
   public  ResponseEntity<?> DeleteFeedbackOfUser(long userId);
    public ResponseEntity<?> GetLastFourofFeedback();

}
