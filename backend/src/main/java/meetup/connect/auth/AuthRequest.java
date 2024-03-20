package meetup.connect.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import meetup.connect.common.converter.ToLowerCase;

public record AuthRequest(
        @NotBlank @ToLowerCase String email,
        @NotBlank String password) {

}