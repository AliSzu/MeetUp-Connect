package meetup.connect.event;

import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link Event}
 */
public record EventCreateDto(String name, LocalDateTime dateFrom, LocalDateTime dateTo,
                             String address) implements Serializable {
}
