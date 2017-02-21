package journals.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import journals.model.Category;
import journals.model.Subscription;
import journals.model.User;
import journals.repository.CategoryRepository;
import journals.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public Optional<User> getUserByLoginName(String loginName) {
		return Optional.ofNullable(userRepository.findByLoginName(loginName));
	}

	@Override
	public void subscribe(User user, Long categoryId) {
		final List<Subscription> subscriptions;
		subscriptions = user.getSubscriptions() != null ? user.getSubscriptions() : new ArrayList<>();
		subscriptions.stream().filter(s -> s.getCategory().getId().equals(categoryId))
			.findFirst()
			.orElseGet(() -> {
			Subscription s = new Subscription();
			s.setUser(user);
			Category category = categoryRepository.findOne(categoryId);
			if (category == null) {
				throw new ServiceException("Category not found");
			}
			s.setCategory(category);
			subscriptions.add(s);
			userRepository.save(user);
			return s;
		});
	}
	
	@Override
	public void unsubscribe(User user, Long categoryId) {
		List<Subscription> subscriptions;
		subscriptions = user.getSubscriptions();
		if (subscriptions == null) {
			subscriptions = new ArrayList<>();
		}
		
		subscriptions.removeIf(s -> s.getCategory().getId().equals(categoryId));
		userRepository.save(user);
	}
	
	@Override
	public User findById(Long id) {
		return userRepository.findOne(id);
	}

}
