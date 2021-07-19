package boutique.controllers;

import boutique.entities.Category;
import boutique.entities.Product;
import boutique.models.CreateProductRequest;
import boutique.models.MessageResponse;
import boutique.repositories.CategoryRepository;
import boutique.repositories.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class ProductController {
    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;

    public ProductController(ProductRepository productRepository,
                             CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/product/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create() {
        try {
            List<String> categories = this.categoryRepository.findAll()
                    .stream().map(x -> x.getName()).collect(Collectors.toList());

            return ResponseEntity.ok(categories);
        }
        catch (Exception ex) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Could not get categories.", Arrays.stream(ex
                            .getStackTrace()).map(x -> x.toString())
                            .collect(Collectors.joining(", "))));
        }
    }

    @PostMapping("/product/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createProcess(@RequestBody CreateProductRequest productRequest) {
        try {
            if (!this.categoryRepository.existsByName(productRequest.getCategoryName())) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Could not find category with name ${productRequest.getCategoryName()}."));
            }

            Category category = this.categoryRepository.findByName(productRequest.getCategoryName());

            Product product = new Product(
                    productRequest.getName(),
                    productRequest.getDescription(),
                    productRequest.getPicture(),
                    productRequest.getPrice(),
                    category);

            this.productRepository.saveAndFlush(product);

            return ResponseEntity.ok(new MessageResponse("Product created successfully!"));
        }
        catch (Exception ex) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Could not create product.", Arrays.stream(ex
                            .getStackTrace()).map(x -> x.toString())
                            .collect(Collectors.joining(", "))));
        }
    }
}
