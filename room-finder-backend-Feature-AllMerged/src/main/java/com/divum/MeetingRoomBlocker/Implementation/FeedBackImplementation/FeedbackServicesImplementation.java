package com.divum.MeetingRoomBlocker.Implementation.FeedBackImplementation;
import com.divum.MeetingRoomBlocker.DTO.FeedBackDTO;
import com.divum.MeetingRoomBlocker.DTO.ResponseDTO;
import com.divum.MeetingRoomBlocker.Entity.FeedbackEntity;
import com.divum.MeetingRoomBlocker.Exception.CustomExceptionHandler;
import com.divum.MeetingRoomBlocker.Exception.DataNotFoundException;
import com.divum.MeetingRoomBlocker.Repository.FeedbackEntityRepository;
import com.divum.MeetingRoomBlocker.Repository.MeetingEntityRepository;
import com.divum.MeetingRoomBlocker.Repository.UserEntityRepository;
import com.divum.MeetingRoomBlocker.Service.FeedBackServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class FeedbackServicesImplementation implements FeedBackServices {
    @Autowired
    private FeedbackEntityRepository feedbackEntityRepository;
    @Autowired
    private MeetingEntityRepository meetingEntityRepository;
    @Autowired
    private UserEntityRepository userEntityRepository;
    @Autowired
    private CustomExceptionHandler customExceptionHandler;
    @Override// Save New FeedBack Of User
    public ResponseEntity SaveFeedBackOfUser(FeedBackDTO feedBackDTO) throws Exception {
        if (feedBackDTO != null) {
            FeedbackEntity newFeedbackEntity = FeedbackEntity.
                    builder().
                    feedback(feedBackDTO.getFeedback()).
                    submittedAt(Timestamp.valueOf((LocalDateTime.now()))).
                    modifiedAt(Timestamp.valueOf(LocalDateTime.now())).
                    createdAt(Timestamp.valueOf(LocalDateTime.now())).
                    rating(feedBackDTO.getRating()).
                    isDeleted(false).
                    meetingEntity(meetingEntityRepository.findById(feedBackDTO.getMeetingId()).get())
                    .userEntity(userEntityRepository.findById(feedBackDTO.getUserId()).get())
                    .build();
            feedbackEntityRepository.save(newFeedbackEntity);
            ResponseDTO responseDTO = ResponseDTO.builder().
                    httpStatus(HttpStatus.OK.getReasonPhrase())
                    .data(newFeedbackEntity).
                    message("New FeedBack Added Successfully").
                    build();
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } else {
            String Messege = "Data not Found While Saving Feedback";
            return customExceptionHandler.HandleDataNotFoundException(new DataNotFoundException(Messege));
        }
    }
    @Override// get Feed Back Of All User
    public ResponseEntity<?> GetFeedBackOfUser() throws Exception {
        List<FeedbackEntity> feedbackEntityList = feedbackEntityRepository.findAll();
        if (feedbackEntityList.isEmpty())
            return customExceptionHandler.HandleDataNotFoundException(new DataNotFoundException("Data not Found While Fetching FeedDatils"));
        else {
            List<FeedBackDTO> FeedofAllUser = new ArrayList<>();
            feedbackEntityList.stream().map(feedbackEntity ->
                            FeedBackDTO.builder()
                                    .UserId(feedbackEntity.getUserEntity().getId())
                                    .MeetingId(feedbackEntity.getMeetingEntity().getId())
                                    .Rating(feedbackEntity.getRating())
                                    .Feedback(feedbackEntity.getFeedback())
                                    .build())
                    .forEach(FeedofAllUser::add);
            ResponseDTO responseDTO = ResponseDTO.builder().data(FeedofAllUser).
                    message("User feedback Retrived Successfully").
                    httpStatus(HttpStatus.OK.getReasonPhrase()).build();
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        }
    }
    @Override //Retrive FeedBack Based On Given Date
    public ResponseEntity<?> getFeedBackOnDate(Timestamp timestamp) {
        List<FeedbackEntity> feedbackEntityList = feedbackEntityRepository.findBySubmittedDate(timestamp);
        if (feedbackEntityList.size() == 0) {
            return customExceptionHandler.HandleDataNotFoundException(new DataNotFoundException("NO data Found on Given Date"));
        } else {
            List<FeedBackDTO> GetFeedBackList=new ArrayList<>();
            feedbackEntityList.stream().map((feedbackEntity -> FeedBackDTO.builder().
                    Feedback(feedbackEntity.getFeedback()).
                    UserId(feedbackEntity.getUserEntity().getId()).
                    MeetingId(feedbackEntity.getMeetingEntity().getId()).
                    Rating(feedbackEntity.getRating()).build())).
                    forEach(GetFeedBackList::add);
            ResponseDTO responseDTO = ResponseDTO.builder().httpStatus(HttpStatus.OK.getReasonPhrase()).
                    data(GetFeedBackList).
                    message("Feedback Details Fetched Successfully")
                    .build();
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        }
    }
    @Override// Update the FeedBack status Either Seen or Not Seen
    public ResponseEntity<?> UpdateFeedBackVisible(long meetingid) {
        Optional<FeedbackEntity> getFeedBack = feedbackEntityRepository.findByMeetingEntity(meetingEntityRepository.findById(meetingid).get());
        if (getFeedBack.isPresent()) {
            getFeedBack.get().setSeen(true);
            feedbackEntityRepository.save(getFeedBack.get());
            ResponseDTO responseDTO = ResponseDTO.builder().
                    httpStatus(HttpStatus.OK.getReasonPhrase()).
                    message("Updation Done on Userfeedback").
                    data(FeedBackDTO.builder().
                            Feedback(getFeedBack.get().getFeedback()).
                            UserId(getFeedBack.get().getUserEntity().getId()).
                            MeetingId(getFeedBack.get().getMeetingEntity().getId()).
                            Rating(getFeedBack.get().getRating()).build()).
                    build();
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } else {
            return customExceptionHandler.HandleDataNotFoundException(new DataNotFoundException("Data Not Found While Updation of Seen status"));
        }
    }
    @Override// Delete FeedbackOfUser
    public ResponseEntity<?> DeleteFeedbackOfUser(long meetingId) {
        Optional<FeedbackEntity> getFeedBack = feedbackEntityRepository.findByMeetingEntity(meetingEntityRepository.findById(meetingId).get());
        if (getFeedBack.isPresent()) {
            getFeedBack.get().setSeen(true);
            feedbackEntityRepository.deleteById(getFeedBack.get().getId());
            ResponseDTO responseDTO = ResponseDTO.builder().
                    httpStatus(HttpStatus.OK.getReasonPhrase()).
                    message("Deletion Of UserFeedBack done ..").
                    data(FeedBackDTO.builder().
                            Feedback(getFeedBack.get().getFeedback()).
                            UserId(getFeedBack.get().getUserEntity().getId()).
                            MeetingId(getFeedBack.get().getMeetingEntity().getId()).
                            Rating(getFeedBack.get().getRating()).build()).
                    build();
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } else {
            return customExceptionHandler.HandleDataNotFoundException(new DataNotFoundException("Data Not Found While Updation of Seen status"));
        }
    }
    @Override
    //Get feedback of Top 10 user
    public ResponseEntity<?> GetLastFourofFeedback() {
        List<FeedbackEntity> FeedBack = feedbackEntityRepository.findTop4ByOrderByModifiedAtDesc();
        if (FeedBack.size() != 0) {
            List<FeedBackDTO> FeedofLastUser = new ArrayList<>();
            FeedBack.stream().map(feedbackEntity ->
                            FeedBackDTO.builder()
                                    .UserId(feedbackEntity.getUserEntity().getId())
                                    .MeetingId(feedbackEntity.getMeetingEntity().getId())
                                    .Rating(feedbackEntity.getRating())
                                    .Feedback(feedbackEntity.getFeedback())
                                    .build())
                    .forEach(FeedofLastUser::add);
            ResponseDTO responseDTO = ResponseDTO.builder().
                    httpStatus(HttpStatus.OK.getReasonPhrase()).
                    message("Data of First 4 FeedBack").
                    data(FeedofLastUser).
                    build();
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } else {
            return customExceptionHandler.HandleDataNotFoundException(new DataNotFoundException("Data Not Found While Fetching Feddback Details"));
        }
    }
}
