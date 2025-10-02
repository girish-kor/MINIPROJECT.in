package miniproject.in.model;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Document(collection = "products")
public class Product {
    @Id
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
    private String filePath;
    private String fileName;
    private Boolean active;

    // Additional metadata fields
    private List<String> imageUrls; // Multiple product images
    private String thumbnailUrl; // Small preview image URL
    private BigDecimal avgReviews; // Average user rating (1-5 scale)
    private List<String> techStack; // Technologies/frameworks used
    private String category; // Main product category
    private List<String> tags; // Keywords for search/filtering
    private Long downloads; // Total download count
    private String licenseType; // License information
    private String sampleFileUrl; // Demo/trial version URL
    private String actualFileUrl; // Full product download/purchase URL
}
