package journals.dto;

import journals.model.User;
import journals.notification.api.EmailType;

public class SingleJournalEmailDTO extends UserEmailDTO<JournalDTO> {

  public SingleJournalEmailDTO(User user, JournalDTO journal) {
    super(user, journal, EmailType.SINGLE);
  }
}
