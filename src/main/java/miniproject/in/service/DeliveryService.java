package miniproject.in.service;

import lombok.RequiredArgsConstructor;
import miniproject.in.model.Order;
import miniproject.in.model.Product;
import miniproject.in.repository.OrderRepository;
import miniproject.in.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final EmailService emailService;

    public void deliverProduct(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getStatus().equals("COMPLETED")) {
            throw new RuntimeException("Order not completed");
        }

        if (order.getDelivered()) {
            throw new RuntimeException("Product already delivered");
        }

        Product product = productRepository.findById(order.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        emailService.sendProductDelivery(order, product);

        order.setDelivered(true);
        orderRepository.save(order);
    }
}
