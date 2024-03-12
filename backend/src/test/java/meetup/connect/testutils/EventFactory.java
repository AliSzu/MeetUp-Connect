package meetup.connect.testutils;

import meetup.connect.event.Event;
import meetup.connect.event.EventDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class EventFactory {

  public static Event create() {
    final Event event = new Event();
    event.setId(new Random().nextLong());
    event.setName("Friendly Picnic");
    event.setAddress("Berlin AlexanderPlatz 223-1");
    event.setDateFrom(LocalDateTime.now());
    event.setDateTo(LocalDateTime.now().plusDays(10));
    event.setCreatedAt(LocalDateTime.now().minusDays(11));

    return event;
  }

  public static EventDto createDto() {
    return new EventDto(
        new Random().nextLong(),
        "Friendly Picnic",
        LocalDateTime.now(),
        LocalDateTime.now().plusDays(10),
        "Berlin AlexanderPlatz",
        LocalDateTime.now().minusDays(11));
  }

  public static List<Event> createEventList(int size) {
    List<Event> eventList = new ArrayList<>();
    for (int i = 0; i < size; i++) {
      eventList.add(create());
    }
    return eventList;
  }

  public static Page<Event> getPage(int page, int size, List<Event> allEvents) {
    Pageable pageRequest = PageRequest.of(page, size);
    int start = (int) pageRequest.getOffset();
    int end = Math.min((start + pageRequest.getPageSize()), allEvents.size());
    List<Event> pageContent = allEvents.subList(start, end);
    return new PageImpl<>(pageContent, pageRequest, allEvents.size());
  }
}
