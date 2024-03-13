package meetup.connect.event;

import java.io.Serializable;
import java.time.LocalDateTime;

public record EventDto(
    Long id,
    String name,
    LocalDateTime dateFrom,
    LocalDateTime dateTo,
    String address,
    LocalDateTime createdAt)
    implements Serializable {

  public static EventDto fromEntity(Event event) {
    return new EventDto(
        event.getId(),
        event.getName(),
        event.getDateFrom(),
        event.getDateTo(),
        event.getAddress(),
        event.getCreatedAt());
  }
}
