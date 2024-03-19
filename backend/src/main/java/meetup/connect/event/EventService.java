package meetup.connect.event;

import meetup.connect.common.exception.MeetUpError;
import meetup.connect.common.exception.MeetUpException;
import meetup.connect.common.page.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class EventService {

  private final EventRepository eventRepository;

  public EventService(EventRepository eventRepository) {
    this.eventRepository = eventRepository;
  }

  public EventDto createEvent(EventCreateDto event) {
    Event createdEvent = eventRepository.save(EventCreateDto.toEntity(event));
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

  public void deleteById(Long id) {
    if (!eventRepository.existsById(id)) {
      throw new MeetUpException(MeetUpError.EVENT_NOT_FOUND);
    }
    eventRepository.deleteById(id);
  }
}
