package miniproject.in.config;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("vercel")
public class VercelConfig {

    /**
     * Configuration specific for running in Vercel environment This allows the application to adapt
     * to Vercel's serverless architecture
     */
    @Bean
    public ServletWebServerFactory servletWebServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();

        // Configure for Vercel's serverless environment
        factory.setContextPath("");
        factory.setPort(8080);

        return factory;
    }
}
