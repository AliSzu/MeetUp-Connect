package meetup.connect.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ControllerExceptionHandler {

  @ExceptionHandler(MeetUpException.class)
  public ResponseEntity<ErrorResponse> handleException(MeetUpException ex) {
    ErrorResponse error = new ErrorResponse(ex.getMeetUpError(), LocalDateTime.now());
    return new ResponseEntity<>(error, ex.getMeetUpError().getHttpStatus());
  }
}
