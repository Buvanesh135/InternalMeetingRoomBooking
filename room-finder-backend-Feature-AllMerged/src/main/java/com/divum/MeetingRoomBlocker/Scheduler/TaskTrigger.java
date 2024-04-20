package com.divum.MeetingRoomBlocker.Scheduler;
import com.divum.MeetingRoomBlocker.Entity.Enum.MeetingStatusEntity;
import com.divum.MeetingRoomBlocker.Entity.MeetingEntity;
import com.divum.MeetingRoomBlocker.Repository.MeetingEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
@Component
public class TaskTrigger  {
    @Autowired
    private TaskScheduler taskScheduler;
    @Autowired
    private MeetingEntityRepository meetingEntityRepository;
    public void scheduleTasksAtSpecifiedTimes(MeetingEntity allMeetings) {
            String time = convertTimestampsToStrings(allMeetings.getEndTime());
            taskScheduler.schedule(() -> performTask(allMeetings.getId()), new CronTrigger(time));
    }
    private String convertTimestampsToStrings(Timestamp endTime) {
        return "0 " + endTime.getMinutes() + " " + endTime.getHours() + " * * *";
    }
    private void performTask(long id) {
        System.out.println("Task for meeting ID: " + id);
        Optional<MeetingEntity> meetingOptional = meetingEntityRepository.findById(id);
        meetingOptional.ifPresent(meeting -> {
            System.out.println("Updating status for meeting ID: " + meeting.getId());
            meeting.setStatus(MeetingStatusEntity.COMPLETED);
            meetingEntityRepository.save(meeting);
            System.out.println("Meeting status updated.");
        });
    }
}
