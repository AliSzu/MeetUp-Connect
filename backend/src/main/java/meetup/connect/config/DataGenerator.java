package meetup.connect.config;

import meetup.connect.event.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.github.javafaker.Faker;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Component
public class DataGenerator implements CommandLineRunner {

  private final EventService eventService;
  private final EventRepository eventRepository;
  private final Faker faker;

  public DataGenerator(EventService eventService, EventRepository eventRepository) {
    this.eventService = eventService;
    this.eventRepository = eventRepository;
    this.faker = new Faker();
  }

  public void generateData() {
    List<Event> events = eventRepository.findAll();
    if (events.isEmpty()) {
      createEvents(4, EventType.PARTY);
      createEvents(10, EventType.CULTURAL_EVENT);
      createEvents(1, EventType.CASUAL_GET_TOGETHER);
    }
  }

  private void createEvents(int timeOffset, EventType eventType) {
    for (int i = 0; i < 4; i++) {
      LocalDateTime randomDate = generateRandomDate(faker);
      EventCreateDto event =
          new EventCreateDto(
              faker.name().title(),
              randomDate.minusHours(timeOffset),
              randomDate,
              faker.address().fullAddress(),
              eventType);
      eventService.createEvent(event);
    }
  }

  private static LocalDateTime generateRandomDate(Faker faker) {
    return LocalDateTime.ofInstant(faker.date().birthday().toInstant(), ZoneId.systemDefault());
  }

  @Override
  public void run(String... args) throws Exception {
    generateData();
  }
}
