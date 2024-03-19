package meetup.connect.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum MeetUpError {
  EVENT_NOT_FOUND("event not found", HttpStatus.NOT_FOUND),
  EMAIL_TAKEN("user with this email already exists", HttpStatus.CONFLICT),
  DATE_WRONG_FORMAT("Date should be in format yyyy-MM-dd HH:mm", HttpStatus.BAD_REQUEST),
  WRONG_DATES("The start date must be before the end date", HttpStatus.BAD_REQUEST),
  WRONG_EVENT_TYPE_WITH_DATE("This event type can only last one day", HttpStatus.BAD_REQUEST),
  THE_SAME_DATE("The start date and the end date must be different", HttpStatus.BAD_REQUEST),
  PAST_DATE("The start date must be in the future", HttpStatus.BAD_REQUEST);

  private final String message;
  private final HttpStatus httpStatus;
  MeetUpError(String message, HttpStatus httpStatus) {
    this.message = message;
    this.httpStatus = httpStatus;
  }
}
