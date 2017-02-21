package journals.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import journals.model.Category;
import journals.model.User;
import journals.repository.CategoryRepository;
import journals.repository.UserRepository;
import journals.service.ServiceException;
import journals.service.UserServiceImpl;

/**
 * @todo missing tests for corner cases
 */
public class UserServiceImplTest {
	
	@Rule
	public MockitoRule rule = MockitoJUnit.rule();
	
	@InjectMocks
	UserServiceImpl userService;
	
	@Mock
	UserRepository userRepo;
	
	@Mock
	CategoryRepository categoryRepo;

	@Test(expected = ServiceException.class)
	public void testSubscribeNoCategory() {
		User user = new User();
		userService.subscribe(user, 12L);
	}

	@Test
	public void testSubscribe() {
		User user = new User();
		when(categoryRepo.findOne(12L)).thenReturn(new Category());
		userService.subscribe(user, 12L);
		verify(userRepo).save(user);
	}

}

