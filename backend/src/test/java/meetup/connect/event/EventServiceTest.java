package meetup.connect.event;

import meetup.connect.common.exception.MeetUpException;
import meetup.connect.common.page.PageResponse;
import meetup.connect.testutils.EventFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

  @InjectMocks private EventService eventService;
  @Mock private EventRepository eventRepository;
  @Mock private EventMapper eventMapper;

  @Test
  @DisplayName("When there are no events, the page should be empty")
  void pageShouldBeEmptyWhenThereIsNoData() {
    List<Event> eventsList = EventFactory.createEventList(0);
    Page<Event> eventsPage = EventFactory.getPage(0, 10, eventsList);

    when(eventRepository.findAll(any(PageRequest.class))).thenReturn(eventsPage);
    PageResponse<EventDto> resultPage = eventService.getPage(0, 10);

    assertTrue(resultPage.getContent().isEmpty());

    verify(eventRepository).findAll(any(PageRequest.class));
  }

  @Test
  @DisplayName("Leftover page should exist when there is excess data for a single page")
  void leftoverPageShouldExistWhenThereIsExcessDataForSinglePage() {
    List<Event> eventsList = EventFactory.createEventList(6);
    Page<Event> eventsPage = EventFactory.getPage(0, 5, eventsList);

    when(eventRepository.findAll(any(PageRequest.class))).thenReturn(eventsPage);
    when(eventMapper.entityToDto(any())).thenReturn(EventFactory.createDto());
    PageResponse<EventDto> resultPage = eventService.getPage(0, 10);

    assertEquals(2, resultPage.getPageable().getTotalPages());

    verify(eventRepository).findAll(any(PageRequest.class));
  }

  @Test
  @DisplayName("When Event does not exists throws MeetUp Exception")
  void shouldThrowMeetUpExceptionWhenEventNotExists() {
    Long nonExistentEventId = 999L;
    when(eventRepository.findById(anyLong())).thenReturn(Optional.empty());
    assertThrows(MeetUpException.class, () -> eventService.getById(nonExistentEventId));
    verify(eventRepository).findById(nonExistentEventId);
  }
}
