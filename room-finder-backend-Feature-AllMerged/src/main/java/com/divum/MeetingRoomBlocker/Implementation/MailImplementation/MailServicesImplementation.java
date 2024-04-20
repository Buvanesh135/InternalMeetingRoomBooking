package com.divum.MeetingRoomBlocker.Implementation.MailImplementation;
import com.divum.MeetingRoomBlocker.DTO.FeddBackSchedulerDTO;
import com.divum.MeetingRoomBlocker.DTO.ResponseDTO;
import com.divum.MeetingRoomBlocker.Entity.Enum.MeetingStatusEntity;
import com.divum.MeetingRoomBlocker.Entity.MeetingEntity;
import com.divum.MeetingRoomBlocker.Entity.UserEntity;
import com.divum.MeetingRoomBlocker.Exception.CustomExceptionHandler;
import com.divum.MeetingRoomBlocker.Exception.DataNotFoundException;
import com.divum.MeetingRoomBlocker.Repository.FeedbackEntityRepository;
import com.divum.MeetingRoomBlocker.Repository.MeetingEntityRepository;
import com.divum.MeetingRoomBlocker.Service.MailServices.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.poi.sl.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Service
public class MailServicesImplementation implements MailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String From;
    private String RejectioMail = "your Booked Meeting was Cancelled by " + From + " due to Some Reason";
    private String Successmail = "Your Booked meeting was Confirmed";
    private String CancelMail="Meeting Sche      duled Was Cancellled due to";
    private String Subject = "Regards ! Meeting Booking  ...";
    @Autowired
    private FeedbackEntityRepository feedbackEntityRepository;
    @Autowired
    private MeetingEntityRepository meetingEntityRepository;
    @Autowired
    private CustomExceptionHandler customExceptionHandler;
    //Send the mail to meeting guest
    @Override
    public ResponseEntity<?> SendMailToMeetingAttenders(long meetingid) {
        Optional<MeetingEntity> getMeeting = meetingEntityRepository.findById(meetingid);
        //System.out.println("getmeeting " + getMeeting.isPresent() + From);
        if (getMeeting.get() != null && getMeeting.get().getStatus().equals(MeetingStatusEntity.REJECTED)){
            ResponseDTO responseDTO= ResponseDTO.builder().
                    httpStatus(HttpStatus.OK.getReasonPhrase()).
                    message("Mail Regarding Rejection of Meeting sent").
                    data(getMeeting).
                    build();
            return new ResponseEntity<>(responseDTO,HttpStatus.OK);
        } else if (getMeeting.get() != null && getMeeting.get().getStatus().equals(MeetingStatusEntity.ACCEPTED)) {
            List<UserEntity> GetAttenders = meetingEntityRepository.findGuestListByMeetingId(meetingid);
           System.out.println("attenders email"+GetAttenders.size());
            for (UserEntity Attender : GetAttenders) {
                if (Attender.getId() != getMeeting.get().getHost().getId()) {
                    String setFrom=From;
                    System.out.println(Attender.getEmail() + "attenders email");
                   String setTo=Attender.getEmail();
                   String setSubject=Subject;
                   String setText="You've have been invited For Meeting at Our Office  " +
                            "\n \n Time:"+getMeeting.get().getStartTime()+"  To  "+getMeeting.get().getEndTime()+"\n  By "+getMeeting.get().getHost().getUsername();
                    SendMail(setFrom,setTo,setText,setSubject);
                }
            }
                    String setTo=getMeeting.get().getHost().getEmail();
                    String setSubject=(Subject);
                    String setText=(Successmail +" At  \n\n" +getMeeting.get().getStartTime()+ " To  "+ getMeeting.get().getEndTime()+"/n Thanks for Booking Ganesan anna");
                    SendMail(From,setTo,setText,setSubject);
                    System.out.println("mail send to all successfully");
                    ResponseDTO responseDTO= ResponseDTO.builder().
                    httpStatus(HttpStatus.OK.getReasonPhrase()).
                    message(" Scheduling  of Meeting Done ...").
                    data(getMeeting).
                    build();
            return new ResponseEntity<>(responseDTO,HttpStatus.OK);
        }
        else {
            return customExceptionHandler.HandleDataNotFoundException(new DataNotFoundException("Data Not found in Meeting Entity"));
        }
    }
    //Cancel the Meeting When Client Meeting is Scheduled
    @Override
    public ResponseEntity<?> MeetingCancel(long meetingid,String Reason) {
        Optional<MeetingEntity> getMeeting = meetingEntityRepository.findById(meetingid);
        if (getMeeting.isPresent()) {
            String SetTo = getMeeting.get().getHost().getEmail();
            String SetText = Reason;
            String SetSubject = "Regards Meeting !";
            SendMail(From,SetTo,SetText,SetSubject);
            ResponseDTO responseDTO = ResponseDTO.builder().
                    data(getMeeting.get()).
                    message(CancelMail + Reason).
                    httpStatus(HttpStatus.OK.getReasonPhrase()).build();
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        }
        else {
            return customExceptionHandler.HandleDataNotFoundException(new DataNotFoundException("Data Not found in Meeting Entity"));
        }
    }
    // Method for Creating Mails
    public void SendMail(String Setfrom,String SetTo,String setText,String Subject)
    {
        SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
        simpleMailMessage.setFrom(Setfrom);
        simpleMailMessage.setTo(SetTo);
        simpleMailMessage.setSubject(Subject);
        simpleMailMessage.setText(setText);
        javaMailSender.send(simpleMailMessage);
    }
    // Method for sending mail for Getting Feedback about Meeting
    @Override
    public void sendFeedBackMail(String host) throws MessagingException {
        String message = "Please visit the following URL: " + "https://forms.gle/C31jCsKyRgBg2knb6";
        // Create MimeMessage
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        // Set email properties
        messageHelper.setFrom(From);
        messageHelper.setTo(host);
        messageHelper.setSubject("Please Fill the FeedBack Regarding Meeting Room!");
        messageHelper.setText(message, false);
        // Send email
        javaMailSender.send(mimeMessage);
    }
    //method for calculating the next Execution time t(Scheduler)
    @Override
    public FeddBackSchedulerDTO calculateNextExecutionTime() {
        System.out.println("inside Calcalute time");
        List<MeetingEntity> Allmeetings=meetingEntityRepository.findAll();
        for(MeetingEntity meeting:Allmeetings)
        {
          return FeddBackSchedulerDTO.builder().
                  Host(meeting.getHost().getEmail()).
                  localDateTime(meeting.getEndTime().toLocalDateTime())
                  .build();
        }
        return FeddBackSchedulerDTO.builder().
                localDateTime( LocalDateTime.now().plusMinutes(30)).Host("").
                build();
    }
    public void readexcel(String filepath) throws IOException {
        FileInputStream file = new FileInputStream(filepath);
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet) {
            for (Cell cell : row) {
                switch (cell.getCellType()) {
                    case STRING:
                        System.out.print(cell.getStringCellValue() + "\t");
                        break;
                    case NUMERIC:
                        System.out.print(cell.getNumericCellValue() + "\t");
                        break;
                    case BOOLEAN:
                        System.out.print(cell.getBooleanCellValue() + "\t");
                        break;
                    default:
                        System.out.print("\t");
                }
            }
        }
        file.close();
    }
}
