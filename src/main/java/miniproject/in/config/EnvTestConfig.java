package miniproject.in.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvTestConfig {
    private static final Logger logger = LoggerFactory.getLogger(EnvTestConfig.class);

    @Value("${SERVER_PORT:Not loaded}")
    private String serverPort;

    @Value("${MONGODB_URI:Not loaded}")
    private String mongodbUri;

    @Value("${JWT_SECRET:Not loaded}")
    private String jwtSecret;

    @Bean
    public CommandLineRunner testEnvVariables() {
        return args -> {
            // Log some environment variables to verify they're loaded
            logger.info("Environment variables loaded:");
            logger.info("SERVER_PORT = {}", serverPort);
            logger.info("MONGODB_URI = {}", mongodbUri);

            // Log a masked version of sensitive values
            String maskedSecret = jwtSecret.length() > 4 ? jwtSecret.substring(0, 4) + "..."
                    + jwtSecret.substring(jwtSecret.length() - 4) : "***";
            logger.info("JWT_SECRET = {}", maskedSecret);
        };
    }
}
