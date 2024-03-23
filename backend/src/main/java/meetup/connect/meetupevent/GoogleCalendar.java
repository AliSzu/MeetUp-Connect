package meetup.connect.meetupevent;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import meetup.connect.meetupevent.MeetUpEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import meetup.connect.user.User;
import org.springframework.util.CollectionUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class GoogleCalendar {
    private static final String APPLICATION_NAME = "MeetUpConnect Calendar";

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    /** Directory to store authorization tokens for this application. */
    private static final String TOKENS_DIRECTORY_PATH = "token";

    private static final String CALENDAR_ID = "primary";
    private static final Logger log = LoggerFactory.getLogger(GoogleCalendar.class);

    /**
     * Global instance of the scopes required by this quickstart. If modifying these scopes, delete
     * your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = List.of(CalendarScopes.CALENDAR_EVENTS);

    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private static Calendar service;

    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {
        // Load client secrets.
        InputStream in = GoogleCalendar.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                        .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                        .setAccessType("offline")
                        .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        // returns an authorized Credential object.
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    private static Calendar getService() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        return new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static String createEvent(MeetUpEvent meetUpEvent) {
        try {
            service = getService();
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
        Event event = fromMeetUpEvent(meetUpEvent);

        try {
            Event createdEvent = service.events().insert(CALENDAR_ID, event).execute();
            return createdEvent.getId();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteEvent(String eventId) {
        try {
            service = getService();
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }

        try {
            service.events().delete(CALENDAR_ID, eventId).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addAttendee(String attendeeEmail, String eventId) {
        try {
            service = getService();
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
        try {

            Event event = service.events().get(CALENDAR_ID, eventId).execute();

            List<EventAttendee> attendees =
                    CollectionUtils.isEmpty(event.getAttendees()) ? new ArrayList<>() : event.getAttendees();
            attendees.add(new EventAttendee().setEmail(attendeeEmail));
            event.setAttendees(attendees);

            service.events().update(CALENDAR_ID, event.getId(), event).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void removeAttendee(String attendeeEmail, String eventId) {
        try {
            service = getService();
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }

        try {

            Event event = service.events().get(CALENDAR_ID, eventId).execute();

            List<EventAttendee> attendees =
                    CollectionUtils.isEmpty(event.getAttendees()) ? new ArrayList<>() : event.getAttendees();
            Optional<EventAttendee> firstAttendeeWithEmail =
                    attendees.stream()
                            .filter(attendee -> attendeeEmail.equals(attendee.getEmail()))
                            .findFirst();
            if (firstAttendeeWithEmail.isPresent()) {
                EventAttendee attendee = firstAttendeeWithEmail.get();
                attendees.remove(attendee);
            } else {
                log.error("Attendee does not exists");
            }
            event.setAttendees(attendees);

            service.events().update(CALENDAR_ID, event.getId(), event).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Event fromMeetUpEvent(MeetUpEvent meetUpEvent) {
        Event googleCalendarEvent =
                new Event().setSummary(meetUpEvent.getName()).setLocation(meetUpEvent.getAddress());

        DateTime startDateTime =
                new DateTime(meetUpEvent.getDateFrom().atOffset(ZoneOffset.UTC).toInstant().toEpochMilli());
        EventDateTime start = new EventDateTime().setDateTime(startDateTime);
        googleCalendarEvent.setStart(start);

        DateTime endDateTime =
                new DateTime(meetUpEvent.getDateTo().atOffset(ZoneOffset.UTC).toInstant().toEpochMilli());
        EventDateTime end = new EventDateTime().setDateTime(endDateTime);
        googleCalendarEvent.setEnd(end);

        googleCalendarEvent.setEnd(end);
        User owner = meetUpEvent.getOwner();
        EventAttendee[] attendees;

        if (owner.isGmailUser()) {
            attendees = new EventAttendee[] {new EventAttendee().setEmail(owner.getEmail())};
        } else {
            attendees =
                    meetUpEvent.getAttendees().stream()
                            .map(user -> new EventAttendee().setEmail(user.getEmail()))
                            .toArray(EventAttendee[]::new);
        }

        googleCalendarEvent.setAttendees(Arrays.asList(attendees));
        return googleCalendarEvent;
    }
}