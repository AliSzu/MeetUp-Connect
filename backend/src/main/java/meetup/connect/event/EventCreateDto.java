package meetup.connect.event;

import java.io.Serializable;
import java.time.LocalDateTime;

public record EventCreateDto(
    String name, LocalDateTime dateFrom, LocalDateTime dateTo, String address)
    implements Serializable {

  public static EventCreateDto fromEntity(Event event) {
    return new EventCreateDto(
        event.getName(), event.getDateFrom(), event.getDateTo(), event.getAddress());
  }

  public static Event toEntity(EventCreateDto eventDto) {
    return new Event(
        null, // ID will be generated automatically
        eventDto.name(),
        eventDto.dateFrom(),
        eventDto.dateTo(),
        eventDto.address(),
        null // createdAt will be generated automatically
        );
  }
}
