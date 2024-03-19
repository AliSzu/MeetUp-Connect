package meetup.connect.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import meetup.connect.common.exception.MeetUpError;
import meetup.connect.common.exception.MeetUpException;
import meetup.connect.user.User;

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

    if (dateFrom != null && dateTo != null && type != null) {

      if (isMultipleDaysCasualEvent(type, dateFrom, dateTo)) {
        throw new MeetUpException(MeetUpError.WRONG_EVENT_TYPE_WITH_DATE);
      }
      if (isEventStartsInTheFuture(dateFrom)) {
        throw new MeetUpException(MeetUpError.PAST_DATE);
      }
      if (isEventStartDateAfterEndDate(dateFrom, dateTo)) {
        throw new MeetUpException(MeetUpError.WRONG_DATES);
      }
      if (isEventStartDateEqualEndDate(dateFrom, dateTo)) {
        throw new MeetUpException(MeetUpError.THE_SAME_DATE);
      }
    }
  }

  public static Event toEntity(EventCreateDto eventDto, User user) {
    return new Event(
        eventDto.name(),
        eventDto.dateFrom(),
        eventDto.dateTo(),
        eventDto.address(),
        eventDto.type(),
            user);
  }

  private boolean isMultipleDaysCasualEvent(
      EventType type, LocalDateTime dateFrom, LocalDateTime dateTo) {
    return (type == EventType.CASUAL_GET_TOGETHER || type == EventType.PARTY)
        && !dateFrom.toLocalDate().isEqual(dateTo.toLocalDate());
  }

  private boolean isEventStartsInTheFuture(LocalDateTime dateFrom) {
    return dateFrom.isBefore(LocalDateTime.now());
  }

  private boolean isEventStartDateAfterEndDate(LocalDateTime dateFrom, LocalDateTime dateTo) {
    return dateFrom.isAfter(dateTo);
  }

  private boolean isEventStartDateEqualEndDate(LocalDateTime dateFrom, LocalDateTime dateTo) {
    return dateFrom.isEqual(dateTo);
  }
}
