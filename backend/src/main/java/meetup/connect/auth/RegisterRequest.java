package meetup.connect.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
  private String username;
  private String email;
  private String password;

  public RegisterRequest(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
  }

  public RegisterRequest() {}
}
