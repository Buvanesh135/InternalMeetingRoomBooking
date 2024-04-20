package com.divum.MeetingRoomBlocker.Controller.NotificationController;

import com.divum.MeetingRoomBlocker.API.NotificationAPI.FeedBackAPI;
import com.divum.MeetingRoomBlocker.DTO.FeedBackDTO;
import com.divum.MeetingRoomBlocker.Entity.FeedbackEntity;
import com.divum.MeetingRoomBlocker.Repository.FeedbackEntityRepository;
import com.divum.MeetingRoomBlocker.Service.FeedBackServices;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
@Controller
@CrossOrigin
@RequiredArgsConstructor
public class FeedBackController implements FeedBackAPI {

    @Autowired
    private final FeedBackServices feedBackServices;

    @Override
    public ResponseEntity<?> Savefeedback(@RequestBody FeedBackDTO feedBackDTO) throws Exception {
        return feedBackServices.SaveFeedBackOfUser(feedBackDTO);
    }
    // Getting the FeedBack of Last 4 User
    @Override
    public ResponseEntity<?> GetAllFeedBack() throws Exception {
        return feedBackServices.GetLastFourofFeedback();
    }

    @Override
    public  ResponseEntity<?> getFeedBackOfDate(@RequestParam("date") String date)
    {
        System.out.println(Timestamp.valueOf(date));
        return feedBackServices.getFeedBackOnDate(Timestamp.valueOf(date));
    }

    @Override
    public ResponseEntity<?> UpdateFeedBackVisible(@PathVariable long user_id)
    {
        return feedBackServices.UpdateFeedBackVisible(user_id);
    }

    @Override
    public  ResponseEntity<?> DeleteFeedBackOfUser(@PathVariable long user_id)
    {
        return feedBackServices.DeleteFeedbackOfUser(user_id);
    }
    //the CronScheduleBuilder allows you to create triggers with cron expressions,
    // but the cron expression itself only defines the schedule (i.e., the time intervals at which the job should run).
    // It doesn't directly set values or parameters inside the cron expression.
//        @GetMapping("/showPage")
//        public String showPage() {
//               return "feedback"; // Assuming you have an HTML file named "myPage.html"
//        }
    //    @PostMapping("/test")
//    public ResponseEntity<?> test(@RequestBody FeedBackDTO feedBackDTO){
//        FeedbackEntity feedbackEntity = FeedbackEntity.builder()
//                .feedback(feedBackDTO.getFeedback())
//                .userEntity(null)
//                .rating(feedBackDTO.getRating())
//                .meetingEntity(null)
//                .submittedAt(new Timestamp(System.currentTimeMillis()))
//                .build();
//        feedbackEntityRepository.save(feedbackEntity);
//        return new ResponseEntity<>("H",HttpStatus.OK);
//    }
}
