package meetup.connect.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum MeetUpError {
  EVENT_NOT_FOUND("event not found", HttpStatus.NOT_FOUND);

  private final String message;
  private final HttpStatus httpStatus;
  MeetUpError(String message, HttpStatus httpStatus) {
    this.message = message;
    this.httpStatus = httpStatus;
  }
}
