package meetup.connect.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import meetup.connect.common.exception.MeetUpError;
import meetup.connect.common.exception.MeetUpException;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

public record EventCreateDto(
    @NotBlank @Size(max = 50, message = "Name is too long") String name,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm") @NotNull
        LocalDateTime dateFrom,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm") @NotNull
        LocalDateTime dateTo,
    @NotBlank @Size(max = 100, message = "Address is too long") String address,
    @NotNull EventType type)
    implements Serializable {

  public EventCreateDto {
    if (!dateTo.isAfter(dateFrom)) {
      throw new MeetUpException(MeetUpError.WRONG_DATES);
    }
    if ((type.equals(EventType.CASUAL_GET_TOGETHER) || type.equals(EventType.PARTY))
        && !dateFrom.toLocalDate().isEqual(dateTo.toLocalDate())) {
      throw new MeetUpException(MeetUpError.WRONG_EVENT_TYPE_WITH_DATE);
    }
    if (dateFrom.isEqual(dateTo)) {
      throw new MeetUpException(MeetUpError.THE_SAME_DATE);
    }
    if (dateFrom.isBefore(LocalDateTime.now())) {
      throw new MeetUpException(MeetUpError.PAST_DATE);
    }
  }

  public static Event toEntity(EventCreateDto eventDto) {
    return new Event(
        null, // ID will be generated automatically
        eventDto.name(),
        eventDto.dateFrom(),
        eventDto.dateTo(),
        eventDto.address(),
        null,
        eventDto.type() // createdAt will be generated automatically
        );
  }
}
