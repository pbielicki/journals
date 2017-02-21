package journals.controller;

import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.security.core.Authentication;

import journals.controller.JournalController;
import journals.model.Journal;
import journals.model.Publisher;
import journals.model.Role;
import journals.model.User;
import journals.repository.JournalRepository;
import journals.repository.UserRepository;
import journals.service.CurrentUser;

/**
 * @todo add asserts
 * @todo add test with valid file
 * @todo remove code duplication to utility class
 *
 */
public class JournalControllerTest {
	
	@Rule
	public MockitoRule rule = MockitoJUnit.rule();
	
	@Mock
	UserRepository userRepository;
	
	@Mock
	JournalRepository journalRepository;
	
	@InjectMocks
	JournalController controller;

	@Mock
	Authentication principal;

	User user;
	
	@Before
	public void before() {
		user = new User();
		user.setRole(Role.PUBLISHER);
		user.setId(1L);
		user.setLoginName("user");
		user.setPwd("pwd");
		user.setSubscriptions(Arrays.asList());
		when(principal.getPrincipal()).thenReturn(new CurrentUser(user));
	}

	@Test(expected = FileNotFoundException.class)
	public void testRenderDocumentFileNotFound() throws Exception {
		Journal journal = new Journal();
		Publisher publisher = new Publisher();
		publisher.setId(1L);
		journal.setPublisher(publisher);
		when(journalRepository.findOne(1L)).thenReturn(journal);
		when(userRepository.findOne(1L)).thenReturn(user);
		controller.renderDocument(principal, 1L);
	}

	@Test
	public void testRenderDocument() throws Exception {
		Journal journal = new Journal();
		Publisher publisher = new Publisher();
		publisher.setId(2L);
		journal.setPublisher(publisher);
		when(journalRepository.findOne(1L)).thenReturn(journal);
		when(userRepository.findOne(1L)).thenReturn(user);
		controller.renderDocument(principal, 1L);
	}

}
