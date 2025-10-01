package miniproject.in.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CheckoutResponse {
    private String sessionId;
    private String checkoutUrl;
    private String orderId; // <-- NEW CHANGES
}
