package meetup.connect.meetupevent;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import meetup.connect.user.User;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
public class MeetUpEvent {
  @Id @GeneratedValue private Long id;
  private String name;
  private LocalDateTime dateFrom;
  private LocalDateTime dateTo;
  private String address;
  private MeetUpEventType type;

  @ManyToOne
  @JoinColumn(name = "owner_id", nullable = false)
  private User owner;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "user_event",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "event_id"))
  private Set<User> attendees = new HashSet<>();

  @CreationTimestamp private LocalDateTime createdAt;
  private String googleCalendarEventId;

  public MeetUpEvent(
      Long id,
      String name,
      LocalDateTime dateFrom,
      LocalDateTime dateTo,
      String address,
      LocalDateTime createdAt,
      MeetUpEventType type,
      User owner,
      Set<User> attendees) {
    this.id = id;
    this.name = name;
    this.dateFrom = dateFrom;
    this.dateTo = dateTo;
    this.address = address;
    this.createdAt = createdAt;
    this.type = type;
    this.owner = owner;
    this.attendees = attendees;
  }

  public MeetUpEvent(
      String name,
      LocalDateTime dateFrom,
      LocalDateTime dateTo,
      String address,
      MeetUpEventType type,
      User owner) {
    this.name = name;
    this.dateFrom = dateFrom;
    this.dateTo = dateTo;
    this.address = address;
    this.type = type;
    this.owner = owner;
  }

  public MeetUpEvent() {}

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MeetUpEvent meetUpEvent = (MeetUpEvent) o;
    return Objects.equals(name, meetUpEvent.name)
        && Objects.equals(dateFrom, meetUpEvent.dateFrom)
        && Objects.equals(dateTo, meetUpEvent.dateTo)
        && Objects.equals(address, meetUpEvent.address)
        && Objects.equals(type, meetUpEvent.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, dateFrom, dateTo, address, type);
  }

  public void addAttendee(User user) {
    this.attendees.add(user);
  }

  public void removeAttendee(User user) {
    this.attendees.remove(user);
  }
}
