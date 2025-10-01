package miniproject.in.health;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Balance;

@Component
public class StripeHealthIndicator implements HealthIndicator {

    @Value("${app.stripe.secret-key}")
    private String stripeSecretKey;

    @Override
    public Health health() {
        try {
            Stripe.apiKey = stripeSecretKey;
            // Just retrieve the balance to check if API key is valid
            Balance.retrieve();
            return Health.up().withDetail("service", "Stripe API").build();
        } catch (StripeException e) {
            return Health.down().withDetail("service", "Stripe API")
                    .withDetail("error", e.getMessage()).build();
        }
    }
}
