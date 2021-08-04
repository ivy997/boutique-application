package boutique.controllers;

import boutique.entities.Category;
import boutique.entities.Product;
import boutique.entities.Review;
import boutique.models.*;
import boutique.repositories.CategoryRepository;
import boutique.repositories.ProductRepository;
import boutique.repositories.ReviewRepository;
import boutique.services.ProductService;
import boutique.services.ReviewService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/product")
public class ProductController {
    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private ReviewRepository reviewRepository;
    private ProductService productService;
    private ReviewService reviewService;

    public ProductController(ProductRepository productRepository,
                             CategoryRepository categoryRepository,
                             ReviewRepository reviewRepository,
                             ProductService productService,
                             ReviewService reviewService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.reviewRepository = reviewRepository;
        this.productService = productService;
        this.reviewService = reviewService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> listProducts(@RequestParam(required = false) String criteria,
                                          @RequestParam(required = false, defaultValue = "12") Integer size,
                                          @RequestParam(required = false) String filter,
                                          @RequestParam(required = false, defaultValue = "1") Integer page) {
        try {
            FilterRequest request = null;
            if (filter == null || filter.isEmpty()) {
                 request = new FilterRequest(size, page, criteria);
            }
            else {
                request = new FilterRequest(size, page,
                        this.categoryRepository.findByName(StringUtils.capitalize(filter)).getId(), criteria);
            }

            Pageable paging = PageRequest.of(request.getPageIndex() - 1,
                    request.getElements(),
                    Sort.by(request.getSortBy()).descending());

            Page<Product> pagedResult = this.productService.listProductsWithFilters(request, paging);

            if (!pagedResult.hasContent()) {
                return ResponseEntity.ok(new MessageResponse("No products have been found."));
            }

            List<ProductResponse> products = parseProducts(pagedResult);

            return ResponseEntity.ok(new ListElementsResponse(products, pagedResult.getTotalPages()));
        }
        catch (Exception ex) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(ex.getMessage(), Arrays.stream(ex
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
    public ResponseEntity<?> details(@PathVariable Integer id,
                                     @RequestParam(required = false, defaultValue = "12") Integer size,
                                     @RequestParam(required = false, defaultValue = "1") Integer page) {
        try {
            if (!this.productRepository.existsById(id)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse(
                                "Error: Could not find product with id ${id}."));
            }

            PaginationRequest request = new PaginationRequest(size, page);

            Pageable paging = PageRequest.of(request.getPageIndex() - 1,
                    request.getElements(),
                    Sort.by(request.getSortBy()).descending());

            Product product = this.productRepository.findById(id).get();

            Page<Review> pagedResult = this.reviewRepository
                    .findAllByProductId(product.getId(), paging);

            List<ReviewResponse> reviews = this.reviewService.parseReviews(pagedResult);

            ProductResponse productResponse = new ProductResponse(
                    product.getId(),
                    product.getName(),
                    product.getDescription(),
                    product.getPicture(),
                    product.getDiscount(),
                    product.getPrice(),
                    new CategoryResponse(product.getCategory().getId(), product.getCategory().getName()),
                    reviews);

            return ResponseEntity.ok(new ProductAndReviewsResponse(productResponse, pagedResult.getTotalPages()));
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

    private List<ProductResponse> parseProducts(Page<Product> pagedResult) {
        List<ProductResponse> products = pagedResult
                .stream().map(pr -> new ProductResponse(
                        pr.getId(),
                        pr.getName(),
                        pr.getDescription(),
                        pr.getPicture(),
                        pr.getDiscount(),
                        pr.getPrice(),
                        new CategoryResponse(pr.getCategory().getId(), pr.getCategory().getName()))
                ).collect(Collectors.toList());

        return products;
    }
}
