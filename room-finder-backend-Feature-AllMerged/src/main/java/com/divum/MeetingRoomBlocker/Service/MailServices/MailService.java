package com.divum.MeetingRoomBlocker.Service.MailServices;


import com.divum.MeetingRoomBlocker.DTO.FeddBackSchedulerDTO;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
public interface MailService {
    //Send the mail to meeting guest
    ResponseEntity<?> SendMailToMeetingAttenders(long meetingid);
    //Cancel the Meeting When Client Meeting is Scheduled
    ResponseEntity<?> MeetingCancel(long meetingid, String Reason);
    void sendFeedBackMail(String host) throws MessagingException;
    public FeddBackSchedulerDTO calculateNextExecutionTime() ;
}
