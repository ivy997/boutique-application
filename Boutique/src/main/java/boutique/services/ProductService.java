package boutique.services;

import boutique.entities.Category;
import boutique.entities.Product;
import boutique.models.FilterRequest;
import boutique.repositories.CategoryRepository;
import boutique.repositories.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;

    public ProductService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    public Page<Product> listProductsWithFilters(FilterRequest filter, Pageable paging) throws Exception {
        Page<Product> allProducts = this.productRepository.findAll(paging);

        if (filter.getCategoryId() != null && filter.getProductKeyword() == null) {
            if (!this.categoryRepository.existsById(filter.getCategoryId())) {
                throw new Exception("Could not find category with this id.");
            }

            return this.filterByCategory(filter.getCategoryId(), paging);
        }
        else if (filter.getCategoryId() == null && filter.getProductKeyword() != null) {
            // Search for products by keyword
            return this.filterByKeyword(allProducts.toList(), filter.getProductKeyword(), paging);
        }
        else if (filter.getCategoryId() != null && filter.getProductKeyword() != null) {
            // Search for products by keyword and categoryId
            Page<Product> page = this.filterByCategory(filter.getCategoryId(), paging);

            return this.filterByKeyword(page.toList(), filter.getProductKeyword(), paging);
        }

        return allProducts;
    }

    private Page<Product> filterByCategory(Integer categoryId, Pageable paging) {
        Category category = this.categoryRepository.findById(categoryId).get();
        Page<Product> pagedResult = this.productRepository.findAllByCategory(category, paging);

        return pagedResult;
    }

    private Page<Product> filterByKeyword(List<Product> products, String keyword, Pageable paging) {
        List<Product> filteredProducts = products
                .stream()
                .filter(x -> x.getName().toLowerCase()
                        .contains(keyword.toLowerCase()))
                .collect(Collectors.toList());

        Page<Product> pagedResult = new PageImpl<Product>(filteredProducts);

        return pagedResult;
    }
}
