package boutique.controllers;

import boutique.entities.Category;
import boutique.models.*;
import boutique.repositories.CategoryRepository;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/categories")
public class CategoryController {
    private CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> listCategories(@RequestParam(required = false, defaultValue = "12") Integer size,
                                            @RequestParam(required = false, defaultValue = "1") Integer page) {
        try
        {
            PaginationRequest request = new PaginationRequest(size, page);

            Pageable paging = PageRequest.of(request.getPageIndex() - 1,
                    request.getElements(),
                    Sort.by(request.getSortBy()).descending());

            List<CategoryResponse> categories = this.categoryRepository.findAll()
                    .stream().map(x -> new CategoryResponse(
                            x.getId(),
                            x.getName(),
                            x.getProducts().stream().map(pr -> new ProductResponse(
                                    pr.getId(),
                                    pr.getName(),
                                    pr.getDescription(),
                                    pr.getPicture(),
                                    pr.getDiscount(),
                                    pr.getPrice())).collect(Collectors.toList())
                    )).collect(Collectors.toList());

            Page<CategoryResponse> pagedResult = new PageImpl<CategoryResponse>(categories);

            return ResponseEntity.ok(new ListElementsResponse(categories, pagedResult.getTotalPages()));
        }
        catch (Exception ex) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Could not list categories.", Arrays.stream(ex
                            .getStackTrace()).map(x -> x.toString())
                            .collect(Collectors.joining(", "))));
        }
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createProcess(@RequestBody CategoryRequest categoryRequest) {
        try {
            Category category = new Category(categoryRequest.getName());

            this.categoryRepository.saveAndFlush(category);

            return ResponseEntity.ok(new MessageResponse("Category created successfully!"));
        }
        catch (Exception ex) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Could not create category.", Arrays.stream(ex
                            .getStackTrace()).map(x -> x.toString())
                            .collect(Collectors.joining(", "))));
        }
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> edit(@PathVariable Integer id) {
        try {
            if (!this.categoryRepository.existsById(id)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Could not find category with this id."));
            }

            Category category = this.categoryRepository.findById(id).get();
            CategoryResponse categoryResponse = new CategoryResponse(category.getId(), category.getName());

            return ResponseEntity.ok(categoryResponse);
        }
        catch (Exception ex) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Could not get category.", Arrays.stream(ex
                            .getStackTrace()).map(x -> x.toString())
                            .collect(Collectors.joining(", "))));
        }
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> editProcess(@RequestBody CategoryRequest categoryRequest, @PathVariable Integer id) {
        try {
            if (!this.categoryRepository.existsById(id)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Could not find category with id ${categoryRequest.getId()}."));
            }

            Category category = this.categoryRepository.findById(id).get();
            category.setName(categoryRequest.getName());

            this.categoryRepository.saveAndFlush(category);

            return ResponseEntity.ok(new MessageResponse("Category edited successfully!"));
        }
        catch (Exception ex) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Could not edit category.", Arrays.stream(ex
                            .getStackTrace()).map(x -> x.toString())
                            .collect(Collectors.joining(", "))));
        }
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            if (!this.categoryRepository.existsById(id)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Could not find category with id ${categoryRequest.getId()}."));
            }

            Category category = this.categoryRepository.findById(id).get();
            CategoryResponse categoryResponse = new CategoryResponse(category.getId(), category.getName());

            return ResponseEntity.ok(categoryResponse);
        }
        catch (Exception ex) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Could not get category.", Arrays.stream(ex
                            .getStackTrace()).map(x -> x.toString())
                            .collect(Collectors.joining(", "))));
        }
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteProcess(@PathVariable Integer id) {
        try {
            if (!this.categoryRepository.existsById(id)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Could not find category with id ${categoryRequest.getId()}."));
            }

            Category category = this.categoryRepository.findById(id).get();

            this.categoryRepository.delete(category);

            return ResponseEntity.ok(new MessageResponse("Category deleted successfully!"));
        }
        catch (Exception ex) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Could not delete category.", Arrays.stream(ex
                            .getStackTrace()).map(x -> x.toString())
                            .collect(Collectors.joining(", "))));
        }
    }
}
