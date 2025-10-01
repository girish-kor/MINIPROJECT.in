package miniproject.in.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Document(collection = "otp_tokens")
public class OtpToken {
    @Id
    private String id;

    @Indexed(unique = true)
    private String email;

    private String otp;
    private Long createdAt;
    private Long expiresAt;
}
