package meetup.connect.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

@RestControllerAdvice
public class ControllerExceptionHandler {

  @ExceptionHandler(MeetUpException.class)
  public ResponseEntity<ErrorResponse> handleException(MeetUpException ex) {
    ErrorResponse error = new ErrorResponse(ex.getMeetUpError(), LocalDateTime.now());
    return new ResponseEntity<>(error, ex.getMeetUpError().getHttpStatus());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMissingParams(MethodArgumentNotValidException ex) {
    String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
    ErrorResponse error =
        new ErrorResponse(
            errorMessage, HttpStatus.BAD_REQUEST, ex.getStatusCode().value(), LocalDateTime.now());
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleJsonParseException(
      HttpMessageNotReadableException ex) {
    Throwable mostSpecificCause = ex.getMostSpecificCause();
    MeetUpError meetUpError;
    if (mostSpecificCause instanceof DateTimeParseException) {
      meetUpError = MeetUpError.DATE_WRONG_FORMAT;
    } else if (mostSpecificCause instanceof MeetUpException) {
      meetUpError = ((MeetUpException) mostSpecificCause).getMeetUpError();
    } else {
      ErrorResponse error =
          new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, 404, LocalDateTime.now());
      return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    ErrorResponse error = new ErrorResponse(meetUpError, LocalDateTime.now());
    return new ResponseEntity<>(error, meetUpError.getHttpStatus());
  }
}
