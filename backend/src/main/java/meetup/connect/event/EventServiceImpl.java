package meetup.connect.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceImpl implements EventService {

  private final EventRepository eventRepository;
  private final EventMapper eventMapper;

  public EventServiceImpl(EventRepository eventRepository, EventMapper eventMapper) {
    this.eventRepository = eventRepository;
    this.eventMapper = eventMapper;
  }

  @Override
  public Event createEvent(EventCreateDto event) {
    Event mappedEvent = eventMapper.dtoToEntity(event);
    return eventRepository.save(mappedEvent);
  }

  @Override
  public Page<Event> getPage(Integer page, Integer size) {
    PageRequest pageRequest = PageRequest.of(page, size);
    return eventRepository.findAll(pageRequest);
  }
}
