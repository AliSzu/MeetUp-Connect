package meetup.connect.user;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public Optional<User> findByEmail(String email) {
    System.out.println(email);
    return userRepository.findByEmail(email);
  }

  public User createUser(User user) {
    return userRepository.save(user);
  }
}
