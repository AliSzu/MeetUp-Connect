package meetup.connect.common.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import org.springframework.stereotype.Component;

@Component
public class ToLowerCaseConverter extends StdConverter<String, String> {
  @Override
  public String convert(String source) {
    return source.toLowerCase();
  }
}
