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
    implements Serializable {}
