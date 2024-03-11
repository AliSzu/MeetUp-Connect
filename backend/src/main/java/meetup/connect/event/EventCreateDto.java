package meetup.connect.event;

import java.io.Serializable;
import java.time.LocalDateTime;

public record EventCreateDto(
    String name, LocalDateTime dateFrom, LocalDateTime dateTo, String address)
    implements Serializable {}
