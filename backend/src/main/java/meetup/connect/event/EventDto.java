package meetup.connect.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

public record EventDto(
    Long id,
    String name,
    LocalDateTime dateFrom,
    LocalDateTime dateTo,
    String address,
    EventType type,
    LocalDateTime createdAt)
    implements Serializable {

  public static EventDto fromEntity(Event event) {
    return new EventDto(
        event.getId(),
        event.getName(),
        event.getDateFrom(),
        event.getDateTo(),
        event.getAddress(),
        event.getType(),
        event.getCreatedAt());
  }
}
