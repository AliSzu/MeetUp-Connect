package meetup.connect.testUtils;

import meetup.connect.event.Event;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

abstract public class EventFactory {

    public static Event create() {
        final Event event = new Event();
        event.setId(new Random().nextLong());
        event.setName("Friendly Picnic");
        event.setAddress("Berlin AlexanderPlatz 223-1");
        event.setDateFrom(LocalDateTime.now());
        event.setDateTo(LocalDateTime.now().plusDays(10));
        event.setCreatedAt(LocalDateTime.now().minusDays(11));

        return event;
    }
}
