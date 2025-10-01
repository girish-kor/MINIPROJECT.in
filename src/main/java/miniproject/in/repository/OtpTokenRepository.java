package miniproject.in.repository;

import miniproject.in.model.OtpToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface OtpTokenRepository extends MongoRepository<OtpToken, String> {
    Optional<OtpToken> findByEmail(String email);

    void deleteByEmail(String email);
}
