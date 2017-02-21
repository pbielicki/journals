package journals.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import journals.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
