package com.divum.MeetingRoomBlocker;

import com.divum.MeetingRoomBlocker.Implementation.MailImplementation.MailServicesImplementation;
import com.divum.MeetingRoomBlocker.Scheduler.FeedBackScheduler;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.support.CronTrigger;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.List;

@SpringBootApplication
@EnableScheduling
public class MeetingRoomBlockerApplication {
	//   @Autowired
//    private MailServicesImplementation mailServicesImplementation;
	@Autowired
	private FeedBackScheduler feedBackScheduler;
//	private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
//	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	public static void main(String[] args) throws IOException, GeneralSecurityException {
		SpringApplication.run(MeetingRoomBlockerApplication.class, args);
	}
	@EventListener(ApplicationReadyEvent.class)
	public void run() throws Exception {
		feedBackScheduler.scheduleTask();
	}
//		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
//			final String range = "Sheet1!A1:B";
//
//			Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, null)
//					.setApplicationName(APPLICATION_NAME)
//					.build();
//			ValueRange response = service.spreadsheets().values()
//					.get("1kHMFy49rklt-bq4Bia5pHNEK2BjpaPA__hZ_QqbyhxY/edit?usp=sharing", range)
//					.execute();
//			List<List<Object>> values = response.getValues();
//			if (values == null || values.isEmpty()) {
//				System.out.println("No data found.");
//			} else {
//				System.out.println("Data:");
//				for (List<Object> row : values) {
//					System.out.printf("%s, %s\n", row.get(0), row.get(1));
//				}
//			}


	//	@EventListener(ApplicationRead@EventListener(ApplicationReadyEvent.class)
//	public  void readExcel() throws IOException {
//		String filepath="https://docs.google.com/spreadsheets/d/1kHMFy49rklt-bq4Bia5pHNEK2BjpaPA__hZ_QqbyhxY/edit?usp=sharing";
//		mailServicesImplementation.readexcel(filepath);
//	}
//	public void notifiva() throws MessagingException {
//		mailServicesImplementation.sendFeedBackMail("ganesan.mukhesh@divum.in");
//	}
//	@EventListener(ApplicationReadyEvent.class)
//	public  void readExcel() throws IOException {
//		String filepath="https://docs.google.com/spreadsheets/d/1kHMFy49rklt-bq4Bia5pHNEK2BjpaPA__hZ_QqbyhxY/edit?usp=sharing";
//		mailServicesImplementation.readexcel(filepath);
//	}

}