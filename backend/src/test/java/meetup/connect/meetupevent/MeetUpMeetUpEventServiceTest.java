package meetup.connect.meetupevent;

import meetup.connect.common.exception.MeetUpException;
import meetup.connect.common.page.PageResponse;
import meetup.connect.testutils.Constants;
import meetup.connect.testutils.EventFactory;
import meetup.connect.testutils.UserFactory;
import meetup.connect.user.User;
import meetup.connect.user.UserService;
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
class MeetUpMeetUpEventServiceTest {

  @InjectMocks private MeetUpEventService meetUpEventService;
  @Mock private UserService userService;
  @Mock private MeetUpEventRepository meetUpEventRepository;

  @Test
  @DisplayName("When there are no events, the page should be empty")
  void pageShouldBeEmptyWhenThereIsNoData() {
    List<MeetUpEvent> eventsList = EventFactory.createEventList(0);
    Page<MeetUpEvent> eventsPage = EventFactory.getPage(0, 10, eventsList);

    when(meetUpEventRepository.findAll(any(PageRequest.class))).thenReturn(eventsPage);
    PageResponse<MeetUpEventDto> resultPage = meetUpEventService.getPage(0, 10);

    assertTrue(resultPage.getContent().isEmpty());

    verify(meetUpEventRepository).findAll(any(PageRequest.class));
  }

  @Test
  @DisplayName("Leftover page should exist when there is excess data for a single page")
  void leftoverPageShouldExistWhenThereIsExcessDataForSinglePage() {
    List<MeetUpEvent> eventsList = EventFactory.createEventList(6);
    Page<MeetUpEvent> eventsPage = EventFactory.getPage(0, 5, eventsList);

    when(meetUpEventRepository.findAll(any(PageRequest.class))).thenReturn(eventsPage);
    PageResponse<MeetUpEventDto> resultPage = meetUpEventService.getPage(0, 10);

    assertEquals(2, resultPage.getPageable().getTotalPages());

    verify(meetUpEventRepository).findAll(any(PageRequest.class));
  }

  @Test
  @DisplayName("When Event does not exists throws MeetUp Exception")
  void shouldThrowMeetUpExceptionWhenEventNotExists() {
    Long nonExistentEventId = 999L;
    when(meetUpEventRepository.findById(anyLong())).thenReturn(Optional.empty());
    assertThrows(MeetUpException.class, () -> meetUpEventService.getById(nonExistentEventId));
    verify(meetUpEventRepository).findById(nonExistentEventId);
  }

  @Test
  @DisplayName("Event's owner should be the user who created the event")
  void eventShouldHaveOwner() {
    MeetUpEventCreateDto event = EventFactory.createDto();
    User user = UserFactory.createUser();
    MeetUpEvent meetUpEventWithUser = MeetUpEventCreateDto.toEntity(event, user);

    when(userService.findByEmail(any(String.class))).thenReturn(user);
    when(meetUpEventRepository.save(any())).thenReturn(meetUpEventWithUser);

    MeetUpEventDto finalEvent = meetUpEventService.createEvent(event, user.getEmail());
    assertEquals(user.getEmail(), finalEvent.owner().email());
  }

  @Test
  @DisplayName("When user tries to delete an event it must be its owner")
  void shouldThrowMeetUpErrorWhenDeleteUserIsNotEventOwner() {
    User user = UserFactory.createUser();
    User user2 = UserFactory.createUser();
    user.setEmail(Constants.RANDOM_EMAIL_1);
    user2.setEmail(Constants.RANDOM_EMAIL_2);
    MeetUpEvent meetUpEvent = EventFactory.create();
    meetUpEvent.setOwner(user);

    when(meetUpEventRepository.findById(any())).thenReturn(Optional.of(meetUpEvent));
    when(userService.findByEmail(any(String.class))).thenReturn(user2);

    assertThrows(
        MeetUpException.class, () -> meetUpEventService.deleteById(meetUpEvent.getId(), user2.getEmail()));
  }
}
