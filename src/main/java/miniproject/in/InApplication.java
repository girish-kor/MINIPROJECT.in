package miniproject.in;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class InApplication {
	private static final Logger logger = LoggerFactory.getLogger(InApplication.class);

	public static void main(String[] args) {
		// Load environment variables from .env file before Spring Boot initializes
		try {
			Dotenv.configure().ignoreIfMissing().load();
			logger.info("Loaded .env file");
		} catch (Exception e) {
			logger.warn("Failed to load .env file: {}", e.getMessage());
		}

		SpringApplication.run(InApplication.class, args);
		logger.info("Backend Server Started...");
	}

}
