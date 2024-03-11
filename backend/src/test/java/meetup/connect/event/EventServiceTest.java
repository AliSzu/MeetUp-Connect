package meetup.connect.event;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

  @InjectMocks private EventService eventService;
  @Mock private EventRepository eventRepository;

  @Test
  @DisplayName("Should fetch events in paginated list")
  void shouldFetchPaginatedEventsList() {

    Page<Event> events = mock(Page.class);
    when(eventRepository.findAll(any(PageRequest.class))).thenReturn(events);

    eventService.getPage(0, 10);

    verify(eventRepository).findAll(any(PageRequest.class));
  }

  @Test
  @DisplayName("When there are no events, the page should be empty")
  void pageShouldBeEmptyWhenThereIsNoData() {
    List<Event> eventsList = EventFactory.createEventList(0);
    Page<Event> eventsPage = EventFactory.getPage(0, 10, eventsList);

    when(eventRepository.findAll(any(PageRequest.class))).thenReturn(eventsPage);
    Page<Event> resultPage = eventService.getPage(0, 10);

    assertTrue(resultPage.isEmpty());

    verify(eventRepository).findAll(any(PageRequest.class));
  }

  @Test
  @DisplayName("Leftover page should exist when there is excess data for a single page")
  void leftoverPageShouldExistWhenThereIsExcessDataForSinglePage() {
    List<Event> eventsList = EventFactory.createEventList(6);
    Page<Event> eventsPage = EventFactory.getPage(0, 5, eventsList);

    when(eventRepository.findAll(any(PageRequest.class))).thenReturn(eventsPage);
    Page<Event> resultPage = eventService.getPage(0, 10);

    assertEquals(2, resultPage.getTotalPages());

    verify(eventRepository).findAll(any(PageRequest.class));
  }
}
