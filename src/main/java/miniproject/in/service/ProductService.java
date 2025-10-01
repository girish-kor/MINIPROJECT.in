package miniproject.in.service;

import java.util.List;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import miniproject.in.exception.ResourceNotFoundException;
import miniproject.in.model.Product;
import miniproject.in.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Cacheable("products")
    public List<Product> getAllActiveProducts() {
        return productRepository.findByActiveTrue();
    }

    @Cacheable(value = "products", key = "#id")
    public Product getProductById(String id) {
        return productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product not found with id: " + id));
    }
}
