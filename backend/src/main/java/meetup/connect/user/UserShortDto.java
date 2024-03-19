package meetup.connect.user;

import java.io.Serializable;

public record UserShortDto(Long id, String name, String email) implements Serializable {}
