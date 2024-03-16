package meetup.connect.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {
  private String email;

  private String password;

  public AuthRequest(String email, String password) {
    this.email = email;
    this.password = password;
  }
  public AuthRequest() {}
}
