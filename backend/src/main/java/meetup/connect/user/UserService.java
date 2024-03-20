package meetup.connect.user;

import meetup.connect.common.exception.MeetUpError;
import meetup.connect.common.exception.MeetUpException;
import meetup.connect.event.EventDto;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User findByEmail(String email) {
    return userRepository
            .findByEmail(email)
            .orElseThrow(() -> new MeetUpException(MeetUpError.EMAIL_NOT_FOUND));
  }

  public User createUser(User user) {
    return userRepository.save(user);
  }

  public boolean checkIfExistsByEmail(String email) {
    return userRepository.existsByEmail(email);
  }

  public Set<UserReadDto> findAll() {
    return userRepository.findAll().stream().map(UserReadDto::fromEntity).collect(Collectors.toSet());
  }

}
