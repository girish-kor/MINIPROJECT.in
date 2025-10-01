package miniproject.in.repository;

import miniproject.in.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface OrderRepository extends MongoRepository<Order, String> {
    Optional<Order> findByStripeSessionId(String stripeSessionId);
}
