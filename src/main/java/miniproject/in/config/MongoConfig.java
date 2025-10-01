package miniproject.in.config;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.lang.NonNull;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Override
    @NonNull
    protected String getDatabaseName() {
        // Extract database name from URI
        String dbName = "miniproject";
        int lastSlashIndex = mongoUri.lastIndexOf('/');
        if (lastSlashIndex != -1) {
            dbName = mongoUri.substring(lastSlashIndex + 1);
            // Remove any query parameters
            int queryIndex = dbName.indexOf('?');
            if (queryIndex != -1) {
                dbName = dbName.substring(0, queryIndex);
            }
        }
        return dbName;
    }

    @Override
    @Bean
    @NonNull
    public MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString(mongoUri);

        MongoClientSettings mongoClientSettings =
                MongoClientSettings.builder().applyConnectionString(connectionString)
                        .applyToConnectionPoolSettings(builder -> builder.maxSize(100).minSize(10)
                                .maxWaitTime(30000, TimeUnit.MILLISECONDS)
                                .maxConnectionLifeTime(30, TimeUnit.MINUTES)
                                .maxConnectionIdleTime(10, TimeUnit.MINUTES))
                        .applyToSocketSettings(
                                builder -> builder.connectTimeout(30000, TimeUnit.MILLISECONDS)
                                        .readTimeout(30000, TimeUnit.MILLISECONDS))
                        .applyToServerSettings(
                                builder -> builder.heartbeatFrequency(20000, TimeUnit.MILLISECONDS))
                        .build();

        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    @NonNull
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), getDatabaseName());
    }
}
