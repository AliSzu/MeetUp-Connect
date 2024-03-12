package meetup.connect.event;

import meetup.connect.common.exception.MeetUpError;
import meetup.connect.common.exception.MeetUpException;
import meetup.connect.common.page.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class EventService {

  private final EventRepository eventRepository;
  private final EventMapper eventMapper;

  public EventService(EventRepository eventRepository, EventMapper eventMapper) {
    this.eventRepository = eventRepository;
    this.eventMapper = eventMapper;
  }

  public Event createEvent(EventCreateDto event) {
    Event mappedEvent = eventMapper.createDtoToEntity(event);
    return eventRepository.save(mappedEvent);
  }

  public PageResponse<EventDto> getPage(Integer page, Integer size) {
    PageRequest pageRequest = PageRequest.of(page, size);
    Page<Event> eventPages = eventRepository.findAll(pageRequest);
    Page<EventDto> eventDtoPages = eventPages.map(eventMapper::entityToDto);
    return new PageResponse<>(eventDtoPages);
  }

  public EventDto getById(Long id) {
    Event event =
        eventRepository
            .findById(id)
            .orElseThrow(() -> new MeetUpException(MeetUpError.EVENT_NOT_FOUND));
    return eventMapper.entityToDto(event);
  }

  public void deleteById(Long id) {
    if (!eventRepository.existsById(id)) {
      throw new MeetUpException(MeetUpError.EVENT_NOT_FOUND);
    }
    eventRepository.deleteById(id);
  }
}
