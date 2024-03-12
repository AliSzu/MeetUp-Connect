package meetup.connect.common.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorResponse {
  private String message;
  private HttpStatus httpStatus;
  private Integer code;
  private LocalDateTime time;

  public ErrorResponse(MeetUpError meetUpError, LocalDateTime time) {
    this.message = meetUpError.getMessage();
    this.code = meetUpError.getHttpStatus().value();
    this.httpStatus = meetUpError.getHttpStatus();
    this.time = time;
  }
}
