package meetup.connect;

import meetup.connect.config.DataGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Component
	public static class DataLoader implements CommandLineRunner {

		private final DataGenerator dataGenerator;

		@Autowired
		public DataLoader(DataGenerator dataGenerator) {
			this.dataGenerator = dataGenerator;
		}

		@Override
		public void run(String... args) {
			dataGenerator.generateData();
		}
	}

}
