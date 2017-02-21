package journals.service;

import java.util.List;

import journals.model.Journal;
import journals.model.Publisher;
import journals.model.User;

public interface JournalService {

  List<Journal> listAll(User user);

  List<Journal> publisherList(Publisher publisher);

  Journal publish(Publisher publisher, Journal journal, Long categoryId);

  void unPublish(Publisher publisher, Long journalId);
}
