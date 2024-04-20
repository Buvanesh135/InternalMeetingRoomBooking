package com.divum.MeetingRoomBlocker.Implementation.GoogleCloudApiImplementation;
import com.divum.MeetingRoomBlocker.Service.GoogleCloudApiService.GoogleCloudService;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import jakarta.validation.constraints.Email;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
public class GoogleCloudApiImplementation implements GoogleCloudService {

    private final HttpTransport httpTransport;
    private final JsonFactory jsonFactory;

    public GoogleCloudApiImplementation(HttpTransport httpTransport, JsonFactory jsonFactory) {
        this.httpTransport = httpTransport;
        this.jsonFactory = jsonFactory;
    }

    
    @Override
    @SneakyThrows
    public Event eventCreation(String accessToken, Event event, String host) {
        Calendar service = getCalendarService(accessToken);
        event.setAnyoneCanAddSelf(false);
        event.setGuestsCanModify(false);
        event.setGuestsCanInviteOthers(false);
        event.setVisibility("private");
        return service.events().insert(host, event).execute();
    }

    @Override
    @SneakyThrows
    public Event getEvent(String accessToken, String eventId, String email) {
        Calendar service = getCalendarService(accessToken);
        return service.events().get(email, eventId).execute();
    }



    public Calendar getCalendarService(String accessToken) {
        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
        return new Calendar.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName("Eventchecking")
                .build();
    }
    @Override
    @SneakyThrows
    public Event updateEvent(String accessToken, String eventId,@Email String email, Event updatedexistingEvent) {

        Calendar service = getCalendarService(accessToken);
        return service.events().update(email, eventId,updatedexistingEvent).execute();
    }

    @Override
    @SneakyThrows
    public void deleteEventinCalendar(String accessToken, String eventId, String email) {
        Calendar service = getCalendarService(accessToken);
        service.events().delete(email, eventId).execute();
    }

}
