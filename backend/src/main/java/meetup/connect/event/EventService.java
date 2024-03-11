package meetup.connect.event;

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
    Event mappedEvent = eventMapper.dtoToEntity(event);
    return eventRepository.save(mappedEvent);
  }

  public PageResponse<Event> getPage(Integer page, Integer size) {
    PageRequest pageRequest = PageRequest.of(page, size);
    Page<Event> events = eventRepository.findAll(pageRequest);
    return new PageResponse<>(events);
  }
}
