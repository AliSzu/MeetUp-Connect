package meetup.connect.user;

import java.io.Serializable;

public record UserShortDto(Long id, String name, String email) implements Serializable {
  public static UserShortDto fromEntity(User user) {
    return new UserShortDto(user.getId(), user.getName(), user.getEmail());
  }
}
