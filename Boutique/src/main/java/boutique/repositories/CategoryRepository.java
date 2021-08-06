package boutique.repositories;

import boutique.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Override
    Category getById(Integer id);

    Boolean existsByName(String name);

    Category findByName(String name);

    Page<Category> findAll(Pageable paging);
}
