package meetup.connect.event;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
public class Event {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;
    private String address;
    @CreationTimestamp
    private LocalDateTime createdAt;

    public Event(Long id, String name, LocalDateTime dateFrom, LocalDateTime dateTo, String address, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.address = address;
        this.createdAt = createdAt;
    }

    public Event() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(name, event.name) && Objects.equals(dateFrom, event.dateFrom) && Objects.equals(dateTo, event.dateTo) && Objects.equals(address, event.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, dateFrom, dateTo, address);
    }
}
