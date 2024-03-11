package meetup.connect.common.page;

import lombok.Getter;
import org.springframework.data.domain.Page;
import java.util.List;

@Getter
public class PageResponse<T> {
  private final List<T> content;
  private final CustomPageable pageable;

  public PageResponse(Page<T> page) {
    this.content = page.getContent();
    this.pageable =
        new CustomPageable(
            page.getPageable().getPageNumber(),
            page.getPageable().getPageSize(),
            page.getTotalPages(),
            page.getTotalElements(),
            page.getSort());
  }
}
