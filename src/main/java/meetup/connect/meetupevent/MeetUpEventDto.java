package meetup.connect.meetupevent;

import meetup.connect.user.User;
import meetup.connect.user.UserShortDto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public record MeetUpEventDto(
    Long id,
    String name,
    LocalDateTime dateFrom,
    LocalDateTime dateTo,
    String address,
    MeetUpEventType type,
    LocalDateTime createdAt,
    UserShortDto owner,
    Set<UserShortDto> attendees)
    implements Serializable {

  public static MeetUpEventDto fromEntity(MeetUpEvent meetUpEvent) {

    return new MeetUpEventDto(
        meetUpEvent.getId(),
        meetUpEvent.getName(),
        meetUpEvent.getDateFrom(),
        meetUpEvent.getDateTo(),
        meetUpEvent.getAddress(),
        meetUpEvent.getType(),
        meetUpEvent.getCreatedAt(),
        new UserShortDto(
            meetUpEvent.getOwner().getId(), meetUpEvent.getOwner().getName(), meetUpEvent.getOwner().getUsername()),
        attendeesFromEntity(meetUpEvent.getAttendees()));
  }

  private static Set<UserShortDto> attendeesFromEntity(Set<User> attendees) {
    return attendees.stream().map(UserShortDto::fromEntity).collect(Collectors.toSet());
  }

}
