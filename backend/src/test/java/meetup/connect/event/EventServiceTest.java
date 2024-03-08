package meetup.connect.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @InjectMocks
    private EventServiceImpl eventServiceImpl;
    @Mock
    private EventRepository eventRepository;

    @Test
    @DisplayName("Should fetch events in paginated list")
    void shouldFetchPaginatedEventsList() {

        Page<Event> events = mock(Page.class);
        when(eventRepository.findAll(any(PageRequest.class))).thenReturn(events);

        eventServiceImpl.getPage(1, 10);

        verify(eventRepository).findAll(any(PageRequest.class));

    }
}