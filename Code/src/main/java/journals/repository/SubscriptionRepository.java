package journals.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import journals.model.Category;
import journals.model.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

  List<Subscription> findByCategory(Category category);
}
