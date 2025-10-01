package miniproject.in.config;

import lombok.RequiredArgsConstructor;
import miniproject.in.model.Product;
import miniproject.in.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

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
        Product product1 = Product.builder()
                .name("Premium Code Editor")
                .description("Advanced code editor with syntax highlighting and IntelliSense")
                .price(new BigDecimal("29.99"))
                .filePath("/products/code-editor.zip")
                .fileName("code-editor.zip")
                .active(true)
                .build();

        Product product2 = Product.builder()
                .name("Database Management Tool")
                .description("Powerful database management and query tool")
                .price(new BigDecimal("49.99"))
                .filePath("/products/db-manager.zip")
                .fileName("db-manager.zip")
                .active(true)
                .build();

        Product product3 = Product.builder()
                .name("Web Scraper Pro")
                .description("Advanced web scraping tool with GUI interface")
                .price(new BigDecimal("39.99"))
                .filePath("/products/web-scraper.jar")
                .fileName("web-scraper.jar")
                .active(true)
                .build();

        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);

        System.out.println("Sample products created successfully!");
    }
}
