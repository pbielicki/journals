package journals.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import journals.controller.PublisherController;
import journals.model.Publisher;
import journals.model.Role;
import journals.model.User;
import journals.repository.PublisherRepository;
import journals.service.CurrentUser;
import journals.service.JournalService;

/**
 * @todo some copy-paste code should go to utility class
 * @todo missing valid scenario test
 */
public class PublisherControllerTest {
	
	@Rule
	public MockitoRule rule = MockitoJUnit.rule();
	
	@Mock
	PublisherRepository publisherRepository;
	
	@Mock
	JournalService journalService;
	
	@InjectMocks
	PublisherController controller;

	@Mock
	Authentication principal;

	@Mock
	MultipartFile file;

	@Mock
	RedirectAttributes redirectAttributes;
	
	@Before
	public void before() {
		User user = new User();
		user.setRole(Role.PUBLISHER);
		user.setId(1L);
		user.setLoginName("user");
		user.setPwd("pwd");
		user.setSubscriptions(Arrays.asList());
		when(principal.getPrincipal()).thenReturn(new CurrentUser(user));
		
		Publisher publisher = new Publisher();
		publisher.setId(1L);
		when(publisherRepository.findByUser(user)).thenReturn(Optional.of(publisher));
	}
	
	@Test
	public void testHandleFileUpload() {
		controller.handleFileUpload("file", 1L, file , redirectAttributes , principal);
		verify(redirectAttributes).addFlashAttribute("message", "You failed to publish file => No InputStream specified");
	}

}
