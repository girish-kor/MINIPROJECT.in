package miniproject.in.config;

import java.math.BigDecimal;
import java.util.Arrays;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import miniproject.in.model.Product;
import miniproject.in.repository.ProductRepository;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Override
    public void run(String... args) {
        if (productRepository.count() == 0) {
            createSampleProducts();
        }
    }

    private void createSampleProducts() {
        Product product1 = Product.builder().name("Premium Code Editor")
                .description("Advanced code editor with syntax highlighting and IntelliSense")
                .price(new BigDecimal("29.99")).filePath("/products/code-editor.zip")
                .fileName("code-editor.zip").active(true)
                // New fields
                .imageUrls(Arrays.asList("https://example.com/images/code-editor-1.jpg",
                        "https://example.com/images/code-editor-2.jpg",
                        "https://example.com/images/code-editor-3.jpg"))
                .thumbnailUrl("https://example.com/thumbnails/code-editor.jpg")
                .avgReviews(new BigDecimal("4.7"))
                .techStack(Arrays.asList("JavaScript", "TypeScript", "React", "Monaco Editor"))
                .category("Development Tools")
                .tags(Arrays.asList("editor", "IDE", "coding", "programming")).downloads(15789L)
                .licenseType("MIT").sampleFileUrl("https://example.com/demo/code-editor-demo.zip")
                .actualFileUrl("https://example.com/download/code-editor-full.zip").build();

        Product product2 = Product.builder().name("Database Management Tool")
                .description("Powerful database management and query tool")
                .price(new BigDecimal("49.99")).filePath("/products/db-manager.zip")
                .fileName("db-manager.zip").active(true)
                // New fields
                .imageUrls(Arrays.asList("https://example.com/images/db-manager-1.jpg",
                        "https://example.com/images/db-manager-2.jpg"))
                .thumbnailUrl("https://example.com/thumbnails/db-manager.jpg")
                .avgReviews(new BigDecimal("4.5"))
                .techStack(Arrays.asList("Java", "SQL", "Hibernate", "JavaFX")).category("Database")
                .tags(Arrays.asList("database", "SQL", "management", "query", "admin"))
                .downloads(8945L).licenseType("Proprietary")
                .sampleFileUrl("https://example.com/demo/db-manager-trial.zip")
                .actualFileUrl("https://example.com/download/db-manager-full.zip").build();

        Product product3 = Product.builder().name("Web Scraper Pro")
                .description("Advanced web scraping tool with GUI interface")
                .price(new BigDecimal("39.99")).filePath("/products/web-scraper.jar")
                .fileName("web-scraper.jar").active(true)
                // New fields
                .imageUrls(Arrays.asList("https://example.com/images/web-scraper-1.jpg",
                        "https://example.com/images/web-scraper-2.jpg",
                        "https://example.com/images/web-scraper-3.jpg",
                        "https://example.com/images/web-scraper-4.jpg"))
                .thumbnailUrl("https://example.com/thumbnails/web-scraper.jpg")
                .avgReviews(new BigDecimal("4.2"))
                .techStack(Arrays.asList("Python", "BeautifulSoup", "Selenium", "JavaFX"))
                .category("Automation")
                .tags(Arrays.asList("scraping", "automation", "data", "crawler", "web"))
                .downloads(12356L).licenseType("GPL")
                .sampleFileUrl("https://example.com/demo/web-scraper-demo.jar")
                .actualFileUrl("https://example.com/download/web-scraper-full.jar").build();

        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);

        System.out.println("Sample products created successfully!");
    }
}
