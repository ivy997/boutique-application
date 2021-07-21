package boutique.services;

import boutique.entities.Category;
import boutique.entities.Product;
import boutique.models.FilterRequest;
import boutique.repositories.CategoryRepository;
import boutique.repositories.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class ProductService {
    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;

    public ProductService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    public Page<Product> listProductsWithFilters(FilterRequest filter, Pageable paging) throws Exception {
        if (filter.getCategoryId() != null) {
            if (!this.categoryRepository.existsById(filter.getCategoryId())) {
                throw new Exception("Could not find category with this id.");
            }

            Category category = this.categoryRepository.findById(filter.getCategoryId()).get();
            Page<Product> pagedResult = this.productRepository.findAllByCategory(category, paging);

            return pagedResult;
        }

        return this.productRepository.findAll(paging);
    }
}
