package miniproject.in.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.stripe.model.checkout.Session;
import lombok.RequiredArgsConstructor;
import miniproject.in.dto.ApiResponse;
import miniproject.in.dto.CheckoutResponse;
import miniproject.in.model.Product;
import miniproject.in.service.JwtService;
import miniproject.in.service.ProductService;
import miniproject.in.service.StripeService;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final StripeService stripeService;
    private final JwtService jwtService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllProducts() {
        List<Product> products = productService.getAllActiveProducts();
        return ResponseEntity.ok(ApiResponse.builder().success(true)
                .message("Products retrieved successfully").data(products).build());
    }

    @PostMapping("/checkout/{productId}")
    public ResponseEntity<ApiResponse> createCheckout(@PathVariable String productId,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            String userEmail = jwtService.extractEmail(token);

            if (!jwtService.isTokenValid(token)) {
                return ResponseEntity.badRequest().body(
                        ApiResponse.builder().success(false).message("Invalid token").build());
            }

            Product product = productService.getProductById(productId);

            Session session = stripeService.createCheckoutSession(product, userEmail);
            String orderId = session.getMetadata().get("orderId");

            CheckoutResponse response = CheckoutResponse.builder().sessionId(session.getId())
                    .checkoutUrl(session.getUrl()).orderId(orderId).build();

            return ResponseEntity.ok(ApiResponse.builder().success(true)
                    .message("Checkout session created").data(response).build());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.builder().success(false).message(e.getMessage()).build());
        }
    }
}
