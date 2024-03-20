package meetup.connect.user;

import meetup.connect.event.Event;
import meetup.connect.event.EventDto;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

/** DTO for {@link User} */
public record UserReadDto(
    Long id, String name, String email, Set<EventDto> organizedEvents, Set<EventDto> events)
    implements Serializable {

  public static UserReadDto fromEntity(User user) {
    return new UserReadDto(
        user.getId(),
        user.getName(),
        user.getEmail(),
        eventSetFromEntity(user.getOrganizedEvents()),
        eventSetFromEntity(user.getEvents()));
  }

  private static Set<EventDto> eventSetFromEntity(Set<Event> events) {
    return events.stream().map(EventDto::fromEntity).collect(Collectors.toSet());
  }
}
