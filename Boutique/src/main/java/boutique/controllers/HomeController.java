package boutique.controllers;

import boutique.models.CategoryResponse;
import boutique.models.MessageResponse;
import boutique.models.ProductResponse;
import boutique.repositories.CategoryRepository;
import boutique.repositories.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class HomeController {
    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;

    public HomeController(ProductRepository productRepository,
                          CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/")
    public ResponseEntity<?> index() {
        try {
            List<ProductResponse> products = this.productRepository.findAll().stream()
                    .map(x -> new ProductResponse(
                            x.getId(),
                            x.getName(),
                            x.getDescription(),
                            x.getPicture(),
                            x.getDiscount(),
                            x.getPrice(),
                            new CategoryResponse(x.getCategory().getId(), x.getCategory().getName())
                    )).sorted(Comparator.comparingInt(ProductResponse::getId).reversed())
                    .collect(Collectors.toList()).subList(0, 10);

            return ResponseEntity.ok(products);
        }
        catch (Exception ex) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Could not list products.", Arrays.stream(ex
                            .getStackTrace()).map(x -> x.toString())
                            .collect(Collectors.joining(", "))));

        }
    }
}
