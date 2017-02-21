package journals.repository;

import journals.model.Role;
import journals.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

  User findByLoginName(String loginName);

  List<User> findByRole(Role role);

  // TODO: change List to Iterable to avoid OOM with large datasets?
  @Query(value = "select * from user where id in (select user_id from subscription where category_id = ?1)", nativeQuery = true)
  List<User> findBySubscriptionCategory(long categoryId);
}
