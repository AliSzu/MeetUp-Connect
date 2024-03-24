package meetup.connect.config;

import meetup.connect.auth.AuthService;
import meetup.connect.meetupevent.*;
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

  private final MeetUpEventService meetUpEventService;
  private final UserService userService;
  private final AuthService authService;
  private final MeetUpEventRepository meetUpEventRepository;
  private final Faker faker;

  public DataGenerator(MeetUpEventService meetUpEventService, UserService userService, AuthService authService, MeetUpEventRepository meetUpEventRepository) {
    this.meetUpEventService = meetUpEventService;
    this.userService = userService;
    this.authService = authService;
    this.meetUpEventRepository = meetUpEventRepository;
    this.faker = new Faker();
  }

  public void generateData() {
    List<MeetUpEvent> meetUpEvents = meetUpEventRepository.findAll();
    User user = userService.createUser(new User(faker.name().toString(), faker.internet().emailAddress(), faker.internet().password()));
    if (meetUpEvents.isEmpty()) {
      createEvents(2, MeetUpEventType.PARTY, user.getEmail());
      createEvents(10, MeetUpEventType.CULTURAL_EVENT, user.getEmail());
      createEvents(1, MeetUpEventType.CASUAL_GET_TOGETHER, user.getEmail());
    }
  }

  private void createEvents(int timeOffset, MeetUpEventType meetUpEventType, String email) {
    for (int i = 0; i < 1; i++) {
      LocalDateTime randomDate = LocalDateTime.now();
      MeetUpEventCreateDto event =
          new MeetUpEventCreateDto(
              faker.name().title(),
              randomDate.plusHours(1),
              randomDate.plusHours(1 + timeOffset),
              faker.address().fullAddress(),
                  meetUpEventType);
      meetUpEventService.createEvent(event, email);
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
