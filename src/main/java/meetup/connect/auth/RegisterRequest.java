package meetup.connect.auth;

import jakarta.validation.constraints.NotBlank;
import meetup.connect.common.converter.ToLowerCase;

public record RegisterRequest(
        @NotBlank String username,
        @NotBlank @ToLowerCase String email,
        @NotBlank String password) {
}
