package meetup.connect.event;

import meetup.connect.common.exception.MeetUpError;
import meetup.connect.common.exception.MeetUpException;
import meetup.connect.common.page.PageResponse;
import meetup.connect.user.User;
import meetup.connect.user.UserRepository;
import meetup.connect.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class EventService {

  private final EventRepository eventRepository;
  private final UserService userService;

  public EventService(EventRepository eventRepository, UserService userService) {
    this.eventRepository = eventRepository;
    this.userService = userService;
  }

  public EventDto createEvent(EventCreateDto event, String email) {
    User user = userService.findByEmail(email);
    Event createdEvent = eventRepository.save(EventCreateDto.toEntity(event, user));
    return EventDto.fromEntity(createdEvent);
  }

  public PageResponse<EventDto> getPage(Integer page, Integer size) {
    PageRequest pageRequest = PageRequest.of(page, size);

    Page<EventDto> entities = eventRepository.findAll(pageRequest).map(EventDto::fromEntity);

    return new PageResponse<>(entities);
  }

  public EventDto getById(Long id) {
    return eventRepository
        .findById(id)
        .map(EventDto::fromEntity)
        .orElseThrow(() -> new MeetUpException(MeetUpError.EVENT_NOT_FOUND));
  }

  public void deleteById(Long id, String email) {
    Event event =
        eventRepository
            .findById(id)
            .orElseThrow(() -> new MeetUpException(MeetUpError.EVENT_NOT_FOUND));
    User user = userService.findByEmail(email);
    if (!isOwnerOfEvent(user, event.getOwner())) {
      throw new MeetUpException(MeetUpError.INSUFFICIENT_PERMISSIONS);
    }
    eventRepository.deleteById(id);
  }

  public void manageEventAttendance(String email, Long id) {
    Event event =
        eventRepository
            .findById(id)
            .orElseThrow(() -> new MeetUpException(MeetUpError.EVENT_NOT_FOUND));
    User user = userService.findByEmail(email);
    if (isAttendingEvent(user, event)) {

      event.getAttendees().remove(user);
    } else {
      event.getAttendees().add(user);
    }
    eventRepository.save(event);
  }

  private boolean isOwnerOfEvent(User user, User eventOwner) {
    return eventOwner.equals(user);
  }

  private boolean isAttendingEvent(User user, Event event) {
    return event.getAttendees().contains(user);
  }
}
