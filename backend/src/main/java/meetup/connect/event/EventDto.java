package meetup.connect.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import meetup.connect.user.User;
import meetup.connect.user.UserShortDto;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public record EventDto(
    Long id,
    String name,
    LocalDateTime dateFrom,
    LocalDateTime dateTo,
    String address,
    EventType type,
    LocalDateTime createdAt,
    UserShortDto owner,
    Set<UserShortDto> attendees)
    implements Serializable {

  public static EventDto fromEntity(Event event) {

    return new EventDto(
        event.getId(),
        event.getName(),
        event.getDateFrom(),
        event.getDateTo(),
        event.getAddress(),
        event.getType(),
        event.getCreatedAt(),
        new UserShortDto(
            event.getOwner().getId(), event.getOwner().getName(), event.getOwner().getUsername()),
        attendeesFromEntity(event.getAttendees()));
  }

  private static Set<UserShortDto> attendeesFromEntity(Set<User> attendees) {
    return attendees.stream().map(UserShortDto::fromEntity).collect(Collectors.toSet());
  }

}
