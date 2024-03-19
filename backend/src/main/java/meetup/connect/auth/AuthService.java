package meetup.connect.auth;

import meetup.connect.common.exception.MeetUpError;
import meetup.connect.common.exception.MeetUpException;
import meetup.connect.user.User;
import meetup.connect.user.UserRepository;
import meetup.connect.user.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final UserService userService;

  public AuthService(
          PasswordEncoder passwordEncoder,
          JwtService jwtService,
          AuthenticationManager authenticationManager, UserService userService) {
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
    this.authenticationManager = authenticationManager;
    this.userService = userService;
  }

  public AuthResponse register(RegisterRequest request) {
    if(userService.checkIfExistsByEmail(request.email())) {
      throw new MeetUpException(MeetUpError.EMAIL_TAKEN);
    }
    User user = new User();
    user.setEmail(request.email());
    user.setPassword(passwordEncoder.encode(request.password()));
    user.setName(request.username());
    userService.createUser(user);
    final String jwtToken = jwtService.generateToken(user);
    return new AuthResponse(jwtToken);
  }

  public AuthResponse authenticate(AuthRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.email(), request.password()));

    User user = userService.findByEmail(request.email());
    final String jwtToken = jwtService.generateToken(user);
    return new AuthResponse(jwtToken);
  }
}
