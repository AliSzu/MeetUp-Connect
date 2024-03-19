package meetup.connect.config;

import meetup.connect.auth.AuthService;
import meetup.connect.auth.RegisterRequest;
import meetup.connect.event.*;
import meetup.connect.user.User;
import meetup.connect.user.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.github.javafaker.Faker;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Component
public class DataGenerator implements CommandLineRunner {

  private final EventService eventService;
  private final UserService userService;
  private final AuthService authService;
  private final EventRepository eventRepository;
  private final Faker faker;

  public DataGenerator(EventService eventService, UserService userService, AuthService authService, EventRepository eventRepository) {
    this.eventService = eventService;
    this.userService = userService;
    this.authService = authService;
    this.eventRepository = eventRepository;
    this.faker = new Faker();
  }

  public void generateData() {
    List<Event> events = eventRepository.findAll();
    User user = userService.createUser(new User(faker.name().toString(), faker.internet().emailAddress(), faker.internet().password()));
    System.out.println(user);
    if (events.isEmpty()) {
      createEvents(2, EventType.PARTY, user.getEmail());
      createEvents(10, EventType.CULTURAL_EVENT, user.getEmail());
      createEvents(1, EventType.CASUAL_GET_TOGETHER, user.getEmail());
    }
  }

  private void createEvents(int timeOffset, EventType eventType, String email) {
    for (int i = 0; i < 4; i++) {
      LocalDateTime randomDate = LocalDateTime.now();
      EventCreateDto event =
          new EventCreateDto(
              faker.name().title(),
              randomDate.plusHours(1),
              randomDate.plusHours(1 + timeOffset),
              faker.address().fullAddress(),
              eventType);
      eventService.createEvent(event, email);
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
