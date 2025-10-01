package miniproject.in.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import miniproject.in.service.DeliveryService;
import miniproject.in.service.StripeService;

@Slf4j
@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
public class WebhookController {

    private final StripeService stripeService;

    private final DeliveryService deliveryService; // <-- Additional changes

    @Value("${app.stripe.webhook-secret}")
    private String webhookSecret;

    @PostMapping("/stripe")
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        try {
            Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);

            switch (event.getType()) {
                case "checkout.session.completed":
                    Session session =
                            (Session) event.getDataObjectDeserializer().getObject().orElse(null);
                    if (session != null) {
                        stripeService.handleSuccessfulPayment(session);
                        log.info("Payment successful for session: {}", session.getId());
                        // AUTOMATE DELIVERY
                        String orderId = session.getMetadata().get("orderId");
                        if (orderId != null) {
                            deliveryService.deliverProduct(orderId);
                            log.info("Product delivered for order: {}", orderId);
                        }
                    }
                    break;
                default:
                    log.info("Unhandled event type: {}", event.getType());
            }

            return ResponseEntity.ok("OK");
        } catch (SignatureVerificationException e) {
            log.error("Invalid signature", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        } catch (Exception e) {
            log.error("Webhook error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Webhook error");
        }
    }
}
