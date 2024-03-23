package meetup.connect.testutils;

import meetup.connect.meetupevent.MeetUpEvent;
import meetup.connect.meetupevent.MeetUpEventCreateDto;
import meetup.connect.meetupevent.MeetUpEventType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class EventFactory {

  public static MeetUpEvent create() {
    final MeetUpEvent meetUpEvent = new MeetUpEvent();
    meetUpEvent.setId(new Random().nextLong());
    meetUpEvent.setName("Friendly Picnic");
    meetUpEvent.setAddress("Berlin AlexanderPlatz 223-1");
    meetUpEvent.setDateFrom(LocalDateTime.now());
    meetUpEvent.setType(MeetUpEventType.CULTURAL_EVENT);
    meetUpEvent.setDateTo(LocalDateTime.now().plusDays(10));
    meetUpEvent.setCreatedAt(LocalDateTime.now().minusDays(11));
    meetUpEvent.setOwner(UserFactory.createUser());

    return meetUpEvent;
  }

  public static MeetUpEventCreateDto createDto() {
    return new MeetUpEventCreateDto(
        "Friendly Picnic",
        LocalDateTime.now().plusHours(2),
        LocalDateTime.now().plusHours(4),
        "Berlin Platz",
        MeetUpEventType.CULTURAL_EVENT);
  }

  public static List<MeetUpEvent> createEventList(int size) {
    List<MeetUpEvent> meetUpEventList = new ArrayList<>();
    for (int i = 0; i < size; i++) {
      meetUpEventList.add(create());
    }
    return meetUpEventList;
  }

  public static Page<MeetUpEvent> getPage(int page, int size, List<MeetUpEvent> allMeetUpEvents) {
    Pageable pageRequest = PageRequest.of(page, size);
    int start = (int) pageRequest.getOffset();
    int end = Math.min((start + pageRequest.getPageSize()), allMeetUpEvents.size());
    List<MeetUpEvent> pageContent = allMeetUpEvents.subList(start, end);
    return new PageImpl<>(pageContent, pageRequest, allMeetUpEvents.size());
  }
}
