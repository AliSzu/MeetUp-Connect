package meetup.connect.event;

import org.springframework.data.domain.Page;

import java.util.List;

public interface EventService {
  Event createEvent(EventCreateDto event);

  Page<Event> getPage(Integer page, Integer size);
}
