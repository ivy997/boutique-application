package boutique.repositories;

import boutique.entities.Category;
import boutique.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Page<Product> findAll(Pageable pageable);

    Page<Product> findAllByCategory(Category category, Pageable pageable);
}
