package meetup.connect.meetupevent;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import meetup.connect.common.exception.MeetUpError;
import meetup.connect.common.exception.MeetUpException;
import meetup.connect.common.page.PageResponse;
import meetup.connect.googleApi.GoogleCalendar;
import meetup.connect.user.User;
import meetup.connect.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneOffset;
import java.util.Arrays;

@Service
public class MeetUpEventService {

  private final MeetUpEventRepository meetUpEventRepository;
  private final UserService userService;
  private final GoogleCalendar googleCalendar;

  public MeetUpEventService(
      MeetUpEventRepository meetUpEventRepository,
      UserService userService,
      GoogleCalendar googleCalendar) {
    this.meetUpEventRepository = meetUpEventRepository;
    this.userService = userService;
    this.googleCalendar = googleCalendar;
  }

  @Transactional
  public MeetUpEventDto createEvent(MeetUpEventCreateDto event, String email) {
    User user = userService.findByEmail(email);
    MeetUpEvent createdMeetUpEvent =
        meetUpEventRepository.save(MeetUpEventCreateDto.toEntity(event, user));
    String id = googleCalendar.createEvent(fromMeetUpEvent(createdMeetUpEvent));
    createdMeetUpEvent.setGoogleCalendarEventId(id);
    meetUpEventRepository.save(createdMeetUpEvent);
    return MeetUpEventDto.fromEntity(createdMeetUpEvent);
  }

  public PageResponse<MeetUpEventDto> getPage(Integer page, Integer size) {
    PageRequest pageRequest = PageRequest.of(page, size);

    Page<MeetUpEventDto> entities =
        meetUpEventRepository.findAll(pageRequest).map(MeetUpEventDto::fromEntity);

    return new PageResponse<>(entities);
  }

  public MeetUpEventDto getById(Long id) {
    return meetUpEventRepository
        .findById(id)
        .map(MeetUpEventDto::fromEntity)
        .orElseThrow(() -> new MeetUpException(MeetUpError.EVENT_NOT_FOUND));
  }

  @Transactional
  public void deleteById(Long id, String email) {
    MeetUpEvent meetUpEvent =
        meetUpEventRepository
            .findById(id)
            .orElseThrow(() -> new MeetUpException(MeetUpError.EVENT_NOT_FOUND));
    User user = userService.findByEmail(email);
    if (!isOwnerOfEvent(user, meetUpEvent.getOwner())) {
      throw new MeetUpException(MeetUpError.INSUFFICIENT_PERMISSIONS);
    }
    googleCalendar.deleteEvent(meetUpEvent.getGoogleCalendarEventId());
    meetUpEventRepository.deleteById(id);
  }

  @Transactional
  public void manageEventAttendance(String email, Long id) {
    MeetUpEvent meetUpEvent =
        meetUpEventRepository
            .findById(id)
            .orElseThrow(() -> new MeetUpException(MeetUpError.EVENT_NOT_FOUND));
    User user = userService.findByEmail(email);
    if (isOwnerOfEvent(user, meetUpEvent.getOwner())) {
      throw new MeetUpException(MeetUpError.OWNER_ATTENDEE);
    }

    if (isAttendingEvent(user, meetUpEvent)) {
      if (user.isGmailUser()) {
        googleCalendar.removeAttendee(email, meetUpEvent.getGoogleCalendarEventId());
      }
      meetUpEvent.removeAttendee(user);
    } else {
      if (user.isGmailUser()) {
        googleCalendar.addAttendee(email, meetUpEvent.getGoogleCalendarEventId());
      }
      meetUpEvent.addAttendee(user);
    }
    meetUpEventRepository.save(meetUpEvent);
  }

  private boolean isOwnerOfEvent(User user, User eventOwner) {
    return eventOwner.equals(user);
  }

  private boolean isAttendingEvent(User user, MeetUpEvent meetUpEvent) {
    return meetUpEvent.getAttendees().contains(user);
  }

  private Event fromMeetUpEvent(MeetUpEvent meetUpEvent) {
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
