package meetup.connect.meetupevent;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import meetup.connect.common.page.PageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Event")
@RequestMapping("/api/events")
public class MeetUpEventController {

  private final MeetUpEventService meetUpEventService;

  public MeetUpEventController(MeetUpEventService meetUpEventService) {
    this.meetUpEventService = meetUpEventService;
  }

  @GetMapping()
  @ResponseStatus(HttpStatus.OK)
  @Operation(description = "Lists events")
  PageResponse<MeetUpEventDto> getEventPage(
      @Parameter(description = "The initial page from which to return the results.")
          @RequestParam(required = false, defaultValue = "0")
          Integer page,
      @Parameter(description = "Number of results to return per page")
          @RequestParam(required = false, defaultValue = "5")
          Integer size) {
    return meetUpEventService.getPage(page, size);
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  @Operation(description = "Get event by ID")
  MeetUpEventDto getEventById(@PathVariable Long id) {
    return meetUpEventService.getById(id);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(description = "Delete event by ID")
  void deleteById(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
    meetUpEventService.deleteById(id, userDetails.getUsername());
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(description = "Create event")
  MeetUpEventDto createEvent(
          @Valid @RequestBody MeetUpEventCreateDto event, @AuthenticationPrincipal UserDetails userDetails) {
    return meetUpEventService.createEvent(event, userDetails.getUsername());
  }

  @PostMapping("/{id}/attendees")
  @ResponseStatus(HttpStatus.OK)
  @Operation(description = "Manage event attendance (sign up or sign out)")
  void manageEventAttendance(
      @AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
    meetUpEventService.manageEventAttendance(userDetails.getUsername(), id);
  }
}
