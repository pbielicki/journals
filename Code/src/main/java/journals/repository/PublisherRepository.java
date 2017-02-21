package journals.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import journals.model.Publisher;
import journals.model.User;

import java.util.Optional;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {

  Optional<Publisher> findByUser(User user);

}
