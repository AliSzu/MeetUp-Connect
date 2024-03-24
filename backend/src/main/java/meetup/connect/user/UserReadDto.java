package meetup.connect.user;

import meetup.connect.meetupevent.MeetUpEvent;
import meetup.connect.meetupevent.MeetUpEventDto;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

/** DTO for {@link User} */
public record UserReadDto(
        Long id, String name, String email, Set<MeetUpEventDto> organizedEvents, Set<MeetUpEventDto> events)
    implements Serializable {

  public static UserReadDto fromEntity(User user) {
    return new UserReadDto(
        user.getId(),
        user.getName(),
        user.getEmail(),
        eventSetFromEntity(user.getOrganizedMeetUpEvents()),
        eventSetFromEntity(user.getMeetUpEvents()));
  }

  private static Set<MeetUpEventDto> eventSetFromEntity(Set<MeetUpEvent> meetUpEvents) {
    return meetUpEvents.stream().map(MeetUpEventDto::fromEntity).collect(Collectors.toSet());
  }
}
