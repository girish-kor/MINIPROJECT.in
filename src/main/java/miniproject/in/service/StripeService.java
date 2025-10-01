package miniproject.in.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import miniproject.in.model.Order;
import miniproject.in.model.Product;
import miniproject.in.repository.OrderRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class StripeService {

    private final OrderRepository orderRepository;

    @Value("${app.stripe.secret-key}")
    private String stripeSecretKey;

    @Value("${app.stripe.success-url}")
    private String successUrl;

    @Value("${app.stripe.cancel-url}")
    private String cancelUrl;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    public Session createCheckoutSession(Product product, String userEmail) {
        try {
            // Create order first
            Order order = Order.builder().userEmail(userEmail).productId(product.getId())
                    .productName(product.getName()).amount(product.getPrice()).status("PENDING")
                    .createdAt(System.currentTimeMillis()).delivered(false).build();

            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT).setSuccessUrl(successUrl)
                    .setCancelUrl(cancelUrl)
                    .addLineItem(SessionCreateParams.LineItem.builder()
                            .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                    .setCurrency("usd")
                                    .setProductData(
                                            SessionCreateParams.LineItem.PriceData.ProductData
                                                    .builder().setName(product.getName())
                                                    .setDescription(product.getDescription())
                                                    .build())
                                    .setUnitAmount(product.getPrice()
                                            .multiply(java.math.BigDecimal.valueOf(100))
                                            .longValue())
                                    .build())
                            .setQuantity(1L).build())
                    .putMetadata("orderId", order.getId()).putMetadata("userEmail", userEmail)
                    .putMetadata("productId", product.getId()).build();

            Session session = Session.create(params);

            // Update order with session ID
            order.setStripeSessionId(session.getId());
            orderRepository.save(order);

            return session;
        } catch (StripeException e) {
            log.error("Failed to create Stripe checkout session", e);
            throw new RuntimeException("Failed to create checkout session");
        }
    }

    public void handleSuccessfulPayment(Session session) {
        String sessionId = session.getId();

        orderRepository.findByStripeSessionId(sessionId).ifPresent(order -> {
            order.setStatus("COMPLETED");
            orderRepository.save(order);
            log.info("Order {} marked as completed", order.getId());
        });
    }
}
