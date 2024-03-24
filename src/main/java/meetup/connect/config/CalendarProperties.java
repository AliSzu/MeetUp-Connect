package meetup.connect.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "calendar.credentials")
@Getter
@Setter
public class CalendarProperties {

  private String client_id;
  private String client_secret;
}
