package boutique.controllers;

import boutique.entities.Product;
import boutique.models.*;
import boutique.repositories.CategoryRepository;
import boutique.repositories.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public ResponseEntity<?> index(@RequestParam(required = false, defaultValue = "12") Integer size,
                                   @RequestParam(required = false, defaultValue = "1") Integer page) {
        try {
            PaginationRequest request = new PaginationRequest(size, page);

            Pageable paging = PageRequest.of(request.getPageIndex() - 1,
                    request.getElements(),
                    Sort.by(request.getSortBy()).descending());

            Page<Product> pagedResult = this.productRepository.findAll(paging);

            List<ProductResponse> products = pagedResult.stream()
                    .map(x -> new ProductResponse(
                            x.getId(),
                            x.getName(),
                            x.getDescription(),
                            x.getPicture(),
                            x.getDiscount(),
                            x.getPrice(),
                            new CategoryResponse(x.getCategory().getId(), x.getCategory().getName())
                    )).collect(Collectors.toList());

            return ResponseEntity.ok(new ListElementsResponse(products, pagedResult.getTotalPages()));
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
