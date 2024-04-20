package com.divum.MeetingRoomBlocker.Scheduler;
import com.divum.MeetingRoomBlocker.DTO.FeddBackSchedulerDTO;
import com.divum.MeetingRoomBlocker.Entity.MeetingEntity;
import com.divum.MeetingRoomBlocker.Repository.MeetingEntityRepository;
import com.divum.MeetingRoomBlocker.Service.FeedBackServices;
import com.divum.MeetingRoomBlocker.Service.MailServices.MailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class FeedBackScheduler {
     @Autowired
     private MailService mailService;
     @Autowired
     private TaskTrigger taskTrigger;
    @Autowired
    private MeetingEntityRepository meetingEntityRepository;
    @Scheduled(fixedDelay = 1000*3*60) // Polls every second to check if it's time to execute
    public void scheduleTask() throws Exception {
        System.out.println(new Date().toString());
        LocalDateTime now = LocalDateTime.now();
        Timestamp startTime = Timestamp.valueOf(now);
        Timestamp endTime = Timestamp.valueOf(now.plusMinutes(30));
        System.out.println(startTime+" "+endTime);
        List<MeetingEntity> meetings= meetingEntityRepository.findMeetingsEndingWithinHalfHour(startTime,endTime);
        List<MeetingEntity> endTimes = new ArrayList<>();
        System.out.println(endTimes+"size "+meetings.size());
        //taskTrigger.scheduleTasksAtSpecifiedTimes(meetings);
    }

}
