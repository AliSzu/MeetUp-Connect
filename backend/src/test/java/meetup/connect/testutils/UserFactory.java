package meetup.connect.testutils;

import meetup.connect.user.User;
import java.util.UUID;

public abstract class UserFactory {

  public static User createUser() {
    final User user = new User();
    user.setId(999L);
    user.setPassword("Password");
    user.setName("Test");
    user.setEmail(UUID.randomUUID() + "@gmail.com");
    return user;
  }
}
