package boutique.controllers;

import boutique.entities.Category;
import boutique.entities.Product;
import boutique.models.CategoryResponse;
import boutique.models.ProductRequest;
import boutique.models.MessageResponse;
import boutique.models.ProductResponse;
import boutique.repositories.CategoryRepository;
import boutique.repositories.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/product")
public class ProductController {
    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;

    public ProductController(ProductRepository productRepository,
                             CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/all")
    public ResponseEntity<?> listProducts() {
        try
        {
            List<ProductResponse> products = this.productRepository.findAll()
                    .stream().map(pr -> new ProductResponse(
                            pr.getId(),
                            pr.getName(),
                            pr.getDescription(),
                            pr.getPicture(),
                            pr.getDiscount(),
                            pr.getPrice(),
                            new CategoryResponse(pr.getCategory().getId(), pr.getCategory().getName()))
                    ).sorted(Comparator.comparingInt(ProductResponse::getId).reversed()).collect(Collectors.toList());

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

    @GetMapping("/create")
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

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createProcess(@RequestBody ProductRequest productRequest) {
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

    @GetMapping("/{id}")
    public ResponseEntity<?> details(@PathVariable Integer id) {
        try {
            if (!this.productRepository.existsById(id)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse(
                                "Error: Could not find product with id ${id}."));
            }

            Product product = this.productRepository.findById(id).get();
            ProductResponse productResponse = new ProductResponse(
                    product.getId(),
                    product.getName(),
                    product.getDescription(),
                    product.getPicture(),
                    product.getDiscount(),
                    product.getPrice(),
                    new CategoryResponse(product.getCategory().getId(), product.getCategory().getName()));

            return ResponseEntity.ok(productResponse);
        }
        catch (Exception ex) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Could not get product details.", Arrays.stream(ex
                            .getStackTrace()).map(x -> x.toString())
                            .collect(Collectors.joining(", "))));
        }
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> edit(@PathVariable Integer id) {
        try {
            if (!this.productRepository.existsById(id)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse(
                                "Error: Could not find product with id ${id}."));
            }

            Product product = this.productRepository.findById(id).get();
            ProductResponse productResponse = new ProductResponse(
                    product.getId(),
                    product.getName(),
                    product.getDescription(),
                    product.getPicture(),
                    product.getDiscount(),
                    product.getPrice(),
                    new CategoryResponse(product.getCategory().getId(), product.getCategory().getName()));

            return ResponseEntity.ok(productResponse);
        }
        catch (Exception ex) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Could not get product details.", Arrays.stream(ex
                            .getStackTrace()).map(x -> x.toString())
                            .collect(Collectors.joining(", "))));
        }
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> editProcess(@PathVariable Integer id, @RequestBody ProductRequest productRequest) {
        try {
            if (!this.productRepository.existsById(id)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse(
                                "Error: Could not find product with id ${id}."));
            }

            Product product = this.productRepository.findById(id).get();
            Category category = this.categoryRepository.findByName(productRequest.getCategoryName());

            product.setName(productRequest.getName());
            product.setDescription(productRequest.getDescription());
            product.setPicture(productRequest.getPicture());
            product.setPrice(productRequest.getPrice());
            product.setDiscount(productRequest.getDiscount());
            product.setCategory(category);

            this.productRepository.saveAndFlush(product);

            return ResponseEntity.ok(new MessageResponse("Product edited successfully!"));
        }
        catch (Exception ex) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Could not edit product.", Arrays.stream(ex
                            .getStackTrace()).map(x -> x.toString())
                            .collect(Collectors.joining(", "))));
        }
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            if (!this.productRepository.existsById(id)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse(
                                "Error: Could not find product with id ${id}."));
            }

            Product product = this.productRepository.findById(id).get();
            ProductResponse productResponse = new ProductResponse(
                    product.getId(),
                    product.getName(),
                    product.getDescription(),
                    product.getPicture(),
                    product.getDiscount(),
                    product.getPrice(),
                    new CategoryResponse(product.getCategory().getId(), product.getCategory().getName()));

            return ResponseEntity.ok(productResponse);
        }
        catch (Exception ex) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Could not get product details.", Arrays.stream(ex
                            .getStackTrace()).map(x -> x.toString())
                            .collect(Collectors.joining(", "))));
        }
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteProcess(@PathVariable Integer id) {
        try {
            if (!this.productRepository.existsById(id)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse(
                                "Error: Could not find product with id ${id}."));
            }

            Product product = this.productRepository.findById(id).get();

            this.productRepository.delete(product);

            return ResponseEntity.ok(new MessageResponse("Product deleted successfully!"));
        }
        catch (Exception ex) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Could not delete product.", Arrays.stream(ex
                            .getStackTrace()).map(x -> x.toString())
                            .collect(Collectors.joining(", "))));
        }
    }
}
