package meetup.connect.event;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import meetup.connect.common.page.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Event")
@RequestMapping("/api/events")
public class EventController {

  private final EventService eventService;

  public EventController(EventService eventService) {
    this.eventService = eventService;
  }

  @GetMapping()
  @Operation(description = "Lists events")
  PageResponse<EventDto> getEventPage(
      @Parameter(description = "The initial page from which to return the results.")
          @RequestParam(required = false, defaultValue = "0")
          Integer page,
      @Parameter(description = "Number of results to return per page")
          @RequestParam(required = false, defaultValue = "5")
          Integer size) {
    return eventService.getPage(page, size);
  }

  @GetMapping("/{id}")
  @Operation(description = "Get event by ID")
  EventDto getEventById(@PathVariable Long id) {
    return eventService.getById(id);
  }
}
