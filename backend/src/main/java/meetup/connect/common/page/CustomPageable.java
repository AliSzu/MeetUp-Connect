package meetup.connect.common.page;

import lombok.Getter;
import org.springframework.data.domain.Sort;

@Getter
public class CustomPageable {
  private Integer pageNumber;
  private Integer pageSize;
  private Integer totalPages;
  private Long totalElements;
  private Sort sort;

  public CustomPageable(Integer pageNumber, Integer pageSize, Integer totalPages, Long totalElements, Sort sort) {
    this.pageNumber = pageNumber;
    this.pageSize = pageSize;
    this.totalPages = totalPages;
    this.totalElements = totalElements;
    this.sort = sort;
  }

  public CustomPageable() {}
}
