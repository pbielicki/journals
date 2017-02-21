package journals.rest;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import journals.dto.SubscriptionDTO;
import journals.model.Category;
import journals.model.Journal;
import journals.model.Publisher;
import journals.model.Subscription;
import journals.model.User;
import journals.repository.CategoryRepository;
import journals.repository.PublisherRepository;
import journals.service.CurrentUser;
import journals.service.JournalService;
import journals.service.UserService;

@RestController
@RequestMapping("/rest/journals")
public class JournalRestService {

	@Autowired
	PublisherRepository publisherRepository;

	@Autowired
	JournalService journalService;

	@Autowired
	UserService userService;

	@Autowired
	CategoryRepository categoryRepository;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Journal>> browse(@AuthenticationPrincipal Principal principal) {
		CurrentUser activeUser = (CurrentUser) ((Authentication) principal).getPrincipal();
		return ResponseEntity.ok(journalService.listAll(activeUser.getUser()));
	}

	@RequestMapping(value = "/published", method = RequestMethod.GET)
	public List<Journal> publishedList(@AuthenticationPrincipal Principal principal) {
		CurrentUser activeUser = (CurrentUser) ((Authentication) principal).getPrincipal();
		Optional<Publisher> publisher = publisherRepository.findByUser(activeUser.getUser());
		return journalService.publisherList(publisher.get());
	}

	@RequestMapping(value = "/unPublish/{id}", method = RequestMethod.DELETE)
	public void unPublish(@PathVariable("id") Long id, @AuthenticationPrincipal Principal principal) {
		CurrentUser activeUser = (CurrentUser) ((Authentication) principal).getPrincipal();
		Optional<Publisher> publisher = publisherRepository.findByUser(activeUser.getUser());
		journalService.unPublish(publisher.get(), id);
	}

	@RequestMapping(value = "/subscriptions")
	public List<SubscriptionDTO> getUserSubscriptions(@AuthenticationPrincipal Principal principal) {
		CurrentUser activeUser = (CurrentUser) ((Authentication) principal).getPrincipal();
		User persistedUser = userService.findById(activeUser.getId());
		List<Subscription> subscriptions = persistedUser.getSubscriptions();
		List<Category> categories = categoryRepository.findAll();
		List<SubscriptionDTO> subscriptionDTOs = new ArrayList<>(categories.size());
		categories.stream().forEach(c -> {
			SubscriptionDTO subscr = new SubscriptionDTO(c);
			Optional<Subscription> subscription = subscriptions.stream().filter(s -> s.getCategory().getId().equals(c.getId())).findFirst();
			subscr.setActive(subscription.isPresent());
			subscriptionDTOs.add(subscr);
		});
		return subscriptionDTOs;
	}

	@RequestMapping(value = "/subscribe/{categoryId}", method = RequestMethod.POST)
	public void subscribe(@PathVariable("categoryId") Long categoryId, @AuthenticationPrincipal Principal principal) {
		CurrentUser activeUser = (CurrentUser) ((Authentication) principal).getPrincipal();
		User user = userService.findById(activeUser.getUser().getId());
		userService.subscribe(user, categoryId);
	}
	
	@RequestMapping(value = "/unsubscribe/{categoryId}", method = RequestMethod.POST)
	public void unsubscribe(@PathVariable("categoryId") Long categoryId, @AuthenticationPrincipal Principal principal) {
		CurrentUser activeUser = (CurrentUser) ((Authentication) principal).getPrincipal();
		User user = userService.findById(activeUser.getUser().getId());
		userService.unsubscribe(user, categoryId);
	}
}
