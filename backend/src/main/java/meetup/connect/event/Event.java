package meetup.connect.event;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import meetup.connect.user.User;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
public class Event {
  @Id @GeneratedValue private Long id;
  private String name;
  private LocalDateTime dateFrom;
  private LocalDateTime dateTo;
  private String address;
  private EventType type;

  @ManyToOne
  @JoinColumn(name = "owner_id", nullable = false)
  private User owner;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "user_event",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "event_id"))
  private Set<User> attendees;

  @CreationTimestamp private LocalDateTime createdAt;

  public Event(
      Long id,
      String name,
      LocalDateTime dateFrom,
      LocalDateTime dateTo,
      String address,
      LocalDateTime createdAt,
      EventType type) {
    this.id = id;
    this.name = name;
    this.dateFrom = dateFrom;
    this.dateTo = dateTo;
    this.address = address;
    this.createdAt = createdAt;
    this.type = type;
  }

  public Event(
      String name,
      LocalDateTime dateFrom,
      LocalDateTime dateTo,
      String address,
      EventType type,
      User owner) {
    this.name = name;
    this.dateFrom = dateFrom;
    this.dateTo = dateTo;
    this.address = address;
    this.type = type;
    this.owner = owner;
  }

  public Event() {}

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Event event = (Event) o;
    return Objects.equals(name, event.name)
        && Objects.equals(dateFrom, event.dateFrom)
        && Objects.equals(dateTo, event.dateTo)
        && Objects.equals(address, event.address)
        && Objects.equals(type, event.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, dateFrom, dateTo, address, type);
  }
}
