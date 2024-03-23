package meetup.connect.meetupevent;

import meetup.connect.common.exception.MeetUpError;
import meetup.connect.common.exception.MeetUpException;
import meetup.connect.common.page.PageResponse;
import meetup.connect.user.User;
import meetup.connect.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MeetUpEventService {

  private final MeetUpEventRepository meetUpEventRepository;
  private final UserService userService;

  public MeetUpEventService(MeetUpEventRepository meetUpEventRepository, UserService userService) {
    this.meetUpEventRepository = meetUpEventRepository;
    this.userService = userService;
  }

  @Transactional
  public MeetUpEventDto createEvent(MeetUpEventCreateDto event, String email) {
    User user = userService.findByEmail(email);
    MeetUpEvent createdMeetUpEvent = meetUpEventRepository.save(MeetUpEventCreateDto.toEntity(event, user));
    String id = GoogleCalendar.createEvent(createdMeetUpEvent);
    createdMeetUpEvent.setGoogleCalendarEventId(id);
    meetUpEventRepository.save(createdMeetUpEvent);
    return MeetUpEventDto.fromEntity(createdMeetUpEvent);
  }

  public PageResponse<MeetUpEventDto> getPage(Integer page, Integer size) {
    PageRequest pageRequest = PageRequest.of(page, size);

    Page<MeetUpEventDto> entities = meetUpEventRepository.findAll(pageRequest).map(MeetUpEventDto::fromEntity);

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
        GoogleCalendar.removeAttendee(email, meetUpEvent.getGoogleCalendarEventId());
      }
      meetUpEvent.removeAttendee(user);
    } else {
      if (user.isGmailUser()) {
        GoogleCalendar.addAttendee(email, meetUpEvent.getGoogleCalendarEventId());
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
}
