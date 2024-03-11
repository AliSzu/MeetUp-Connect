package meetup.connect.config;

import meetup.connect.event.Event;
import meetup.connect.event.EventCreateDto;
import meetup.connect.event.EventRepository;
import meetup.connect.event.EventService;
import org.springframework.beans.factory.annotation.Autowired;
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
      for (int i = 0; i < 10; i++) {
        EventCreateDto event =
            new EventCreateDto(
                faker.name().title(),
                LocalDateTime.ofInstant(
                    faker.date().birthday().toInstant(), ZoneId.systemDefault()),
                LocalDateTime.ofInstant(
                    faker.date().birthday().toInstant(), ZoneId.systemDefault()),
                faker.address().fullAddress());
        eventService.createEvent(event);
      }
    }
  }

  @Override
  public void run(String... args) throws Exception {
    generateData();
  }
}
