package meetup.connect.googleApi;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.vavr.control.Try;
import meetup.connect.common.exception.MeetUpError;
import meetup.connect.common.exception.MeetUpException;
import meetup.connect.config.CalendarProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static meetup.connect.googleApi.GoogleServiceCircuitBreaker.GOOGLE_CALENDAR_CIRCUIT_BREAKER;


@Service
public class GoogleCalendar {
  private static final String APPLICATION_NAME = "MeetUpConnect Calendar";

  /** Global instance of the JSON factory. */
  private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

  /** Directory to store authorization tokens for this application. */
  private static final String TOKENS_DIRECTORY_PATH = "token";
  private static final Integer PORT = 8888;

  private static final String CALENDAR_ID = "primary";
  private static final Logger log = LoggerFactory.getLogger(GoogleCalendar.class);

  /**
   * Global instance of the scopes required by this quickstart. If modifying these scopes, delete
   * your previously saved tokens/ folder.
   */
  private static final List<String> SCOPES = List.of(CalendarScopes.CALENDAR_EVENTS);

  private final CalendarProperties calendarProperties;
  private final CircuitBreaker circuitBreaker;
  private static Calendar service;

  public GoogleCalendar(CalendarProperties calendarProperties, @Qualifier(value = GOOGLE_CALENDAR_CIRCUIT_BREAKER)CircuitBreaker circuitBreaker) {

    this.calendarProperties = calendarProperties;
    this.circuitBreaker = circuitBreaker;
  }

  private Credential getCredentials() throws IOException {
    GoogleAuthorizationCodeFlow flow =
        new GoogleAuthorizationCodeFlow.Builder(
                new NetHttpTransport(),
                GsonFactory.getDefaultInstance(),
                calendarProperties.getClient_id(),
                calendarProperties.getClient_secret(),
                SCOPES)
            .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
            .setAccessType("offline")
            .build();
    LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(PORT).build();
    // returns an authorized Credential object.
    return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
  }

  private Calendar getService() throws GeneralSecurityException, IOException {
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    var requestFactory = HTTP_TRANSPORT
            .createRequestFactory(httpRequest -> httpRequest
                    .setConnectTimeout(10)
                    .setReadTimeout(5)
                    .setNumberOfRetries(3)
            )
            .getTransport();
    return new Calendar.Builder(requestFactory, JSON_FACTORY, getCredentials())
            .setApplicationName(APPLICATION_NAME)
            .build();
  }

  private Supplier<Calendar> getGoogleCalendarSupplier() {

    Supplier<Calendar> googleApiCall = () -> {
      try {
        service = getService();
        return service;
      } catch (GeneralSecurityException | IOException e) {
        throw new RuntimeException(e);
      }
    };
      return CircuitBreaker.decorateSupplier(circuitBreaker, googleApiCall);
  }


  public String createEvent(Event event) {
    Supplier<Calendar> googleCalendarService  = getGoogleCalendarSupplier();

    Try<Event> tryCreateEvent = Try.ofSupplier(googleCalendarService)
            .mapTry(calendar -> calendar.events().insert(CALENDAR_ID, event).execute())
            .onFailure(this::handleFailure);

    return tryCreateEvent.get().getId();
  }

  public void deleteEvent(String eventId) {
    Supplier<Calendar> googleCalendarServiceSupplier  = getGoogleCalendarSupplier();

    Try<Void> tryDeleteEvent = Try.ofSupplier(googleCalendarServiceSupplier)
            .mapTry(calendar -> calendar.events().delete(CALENDAR_ID, eventId).execute())
            .onFailure(this::handleFailure);

    tryDeleteEvent.get();
  }

  private Event getEvent(String eventId) {
    Supplier<Calendar> googleCalendarServiceSupplier  = getGoogleCalendarSupplier();

    Try<Event> tryGetEvent = Try.ofSupplier(googleCalendarServiceSupplier)
            .mapTry(calendar -> calendar.events().get(CALENDAR_ID, eventId).execute())
            .onFailure(this::handleFailure);

    return tryGetEvent.get();
  }

  public void addAttendee(String attendeeEmail, String eventId) {
    Supplier<Calendar> googleCalendarServiceSupplier  = getGoogleCalendarSupplier();
    Event event = getEvent(eventId);

    List<EventAttendee> attendees =
            CollectionUtils.isEmpty(event.getAttendees()) ? new ArrayList<>() : event.getAttendees();
    attendees.add(new EventAttendee().setEmail(attendeeEmail));
    event.setAttendees(attendees);

    Try<Event> tryAddAttendee = Try.ofSupplier(googleCalendarServiceSupplier)
            .mapTry(calendar -> calendar.events().update(CALENDAR_ID, event.getId(), event).execute())
            .onFailure(this::handleFailure);

    tryAddAttendee.get();
  }



  public void removeAttendee(String attendeeEmail, String eventId) {
    Supplier<Calendar> googleCalendarServiceSupplier  = getGoogleCalendarSupplier();
    Event event = getEvent(eventId);
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
    Try<Event> tryRemoveAttendee = Try.ofSupplier(googleCalendarServiceSupplier)
            .mapTry(calendar -> calendar.events().update(CALENDAR_ID, event.getId(), event).execute())
            .onFailure(this::handleFailure);

    tryRemoveAttendee.get();
  }

  private void handleFailure(Throwable cause) {
    throw new MeetUpException(MeetUpError.GOOGLE_CALENDAR);
  }
}
