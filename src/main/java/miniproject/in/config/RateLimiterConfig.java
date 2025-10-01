package miniproject.in.config;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.annotation.Configuration;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;

@Configuration
public class RateLimiterConfig {

    private final Map<String, Bucket> ipToBucket = new ConcurrentHashMap<>();

    public Bucket resolveBucket(String ipAddress) {
        return ipToBucket.computeIfAbsent(ipAddress, this::createNewBucket);
    }

    private Bucket createNewBucket(String ipAddress) {
        // 50 requests per minute rate limit
        Bandwidth limit = Bandwidth.classic(50, Refill.greedy(50, Duration.ofMinutes(1)));
        return Bucket.builder().addLimit(limit).build();
    }
}
