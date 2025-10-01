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
        
        try {
            ConnectionString connString = new ConnectionString(mongoUri);
            String database = connString.getDatabase();
            
            if (database != null && !database.isEmpty()) {
                dbName = database;
            } else {
                // Fallback to manual parsing if ConnectionString doesn't provide database
                int lastSlashIndex = mongoUri.lastIndexOf('/');
                if (lastSlashIndex != -1 && lastSlashIndex < mongoUri.length() - 1) {
                    String afterSlash = mongoUri.substring(lastSlashIndex + 1);
                    // Remove any query parameters
                    int queryIndex = afterSlash.indexOf('?');
                    if (queryIndex != -1) {
                        dbName = afterSlash.substring(0, queryIndex);
                    } else {
                        dbName = afterSlash;
                    }
                }
            }
        } catch (Exception e) {
            // If any error occurs, use the default database name
            System.out.println("Failed to extract database name from URI: " + e.getMessage());
        }
        
        // Ensure database name is not empty
        if (dbName == null || dbName.trim().isEmpty()) {
            dbName = "miniproject";
        }
        
        System.out.println("Using MongoDB database: " + dbName);
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
