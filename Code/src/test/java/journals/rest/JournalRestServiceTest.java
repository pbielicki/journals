package journals.rest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;

import journals.model.Category;
import journals.model.Publisher;
import journals.model.Role;
import journals.model.User;
import journals.repository.CategoryRepository;
import journals.repository.PublisherRepository;
import journals.rest.JournalRestService;
import journals.service.CurrentUser;
import journals.service.JournalService;
import journals.service.UserService;

/**
 * @todo not all methods tested
 * @todo missing some paths validation
 * @todo missing corner case tests
 */
public class JournalRestServiceTest {
	
	@Rule
	public MockitoRule rule = MockitoJUnit.rule();
	
	@Mock
	PublisherRepository publisherRepository;

	@Mock
	JournalService journalService;

	@Mock
	UserService userService;

	@Mock
	CategoryRepository categoryRepository;
	
	@Mock
	Authentication principal;
	
	@InjectMocks
	JournalRestService rest;

	User user;

	Publisher publisher;

	@Before
	public void before() {
		user = new User();
		user.setRole(Role.PUBLISHER);
		user.setId(1L);
		user.setLoginName("user");
		user.setPwd("pwd");
		user.setSubscriptions(Arrays.asList());
		when(principal.getPrincipal()).thenReturn(new CurrentUser(user));
		when(userService.findById(anyLong())).thenReturn(user);
		
		List<Category> categories = new ArrayList<>();
		Category cat = new Category();
		cat.setId(1L);
		cat.setName("cat1");
		categories.add(cat);
		cat = new Category();
		cat.setId(2L);
		cat.setName("cat2");
		categories.add(cat);
		cat = new Category();
		cat.setId(3L);
		cat.setName("cat3");
		categories.add(cat);
		when(categoryRepository.findAll()).thenReturn(categories);

		publisher = new Publisher();
		when(publisherRepository.findByUser(user)).thenReturn(Optional.of(publisher));
	}
	
	@Test
	public void testBrowse() {
		assertThat(rest.browse(principal).getStatusCode(), is(HttpStatus.OK));
		verify(journalService).listAll(user);
	}

	@Test
	public void testPublishedList() {
		rest.publishedList(principal);
	}

	@Test
	public void testUnPublish() {
		rest.unPublish(1L, principal);
		verify(journalService).unPublish(publisher, 1L);
	}

	@Test
	public void testGetUserSubscriptions() {
		assertThat(rest.getUserSubscriptions(principal).size(), is(3));
		verify(userService).findById(user.getId());
		verify(categoryRepository).findAll();
	}
}
