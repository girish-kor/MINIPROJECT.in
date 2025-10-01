package miniproject.in.config;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.StandardEnvironment;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;

@Configuration
public class DotenvConfig {
    private static final Logger logger = LoggerFactory.getLogger(DotenvConfig.class);
    private final ConfigurableEnvironment environment;

    public DotenvConfig(ConfigurableEnvironment environment) {
        this.environment = environment;
    }

    @PostConstruct
    public void loadEnvVariables() {
        logger.info("Loading environment variables from .env file");
        try {
            Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
            Map<String, Object> envVariables = new HashMap<>();

            // Add all variables from .env to Spring environment
            dotenv.entries().forEach(entry -> {
                envVariables.put(entry.getKey(), entry.getValue());

                // Map specific environment variables to application properties
                mapEnvToAppProperty(entry.getKey(), entry.getValue(), envVariables);

                logger.debug("Loaded env variable: {} (length: {})", entry.getKey(),
                        entry.getValue().length());
            });

            // Add the map of variables to Spring environment with highest precedence
            MutablePropertySources propertySources = environment.getPropertySources();
            if (propertySources
                    .contains(StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME)) {
                propertySources.addBefore(
                        StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME,
                        new MapPropertySource("dotenv", envVariables));
            } else {
                propertySources.addFirst(new MapPropertySource("dotenv", envVariables));
            }

            logger.info("Successfully loaded {} environment variables from .env file",
                    envVariables.size());
        } catch (Exception e) {
            logger.error("Failed to load .env file: {}", e.getMessage(), e);
        }
    }

    /**
     * Maps environment variables to application properties
     */
    private void mapEnvToAppProperty(String envKey, String envValue,
            Map<String, Object> envVariables) {
        // Map JWT properties
        if ("JWT_SECRET".equals(envKey)) {
            envVariables.put("app.jwt.secret", envValue);
        } else if ("JWT_EXPIRATION".equals(envKey)) {
            envVariables.put("app.jwt.expiration", envValue);
        }

        // Map Stripe properties
        else if ("STRIPE_SECRET_KEY".equals(envKey)) {
            envVariables.put("app.stripe.secret-key", envValue);
        } else if ("STRIPE_PUBLISHABLE_KEY".equals(envKey)) {
            envVariables.put("app.stripe.publishable-key", envValue);
        } else if ("STRIPE_WEBHOOK_SECRET".equals(envKey)) {
            envVariables.put("app.stripe.webhook-secret", envValue);
        } else if ("STRIPE_SUCCESS_URL".equals(envKey)) {
            envVariables.put("app.stripe.success-url", envValue);
        } else if ("STRIPE_CANCEL_URL".equals(envKey)) {
            envVariables.put("app.stripe.cancel-url", envValue);
        }

        // Map OTP properties
        else if ("OTP_EXPIRATION".equals(envKey)) {
            envVariables.put("app.otp.expiration", envValue);
        }
    }
}
