package journals.service;

import java.util.Optional;

import journals.model.User;

public interface UserService {

  Optional<User> getUserByLoginName(String loginName);

  void subscribe(User user, Long categoryId);

  void unsubscribe(User user, Long categoryId);

  User findById(Long id);

}