package miniproject.in.model;

import java.math.BigDecimal;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Document(collection = "orders")
public class Order {
    @Id
    private String id;
    private String userEmail;
    private String productId;
    private String productName;
    private BigDecimal amount;
    private String stripeSessionId;
    private String status; // PENDING, COMPLETED, FAILED
    private Long createdAt;
    private Boolean delivered;
}
