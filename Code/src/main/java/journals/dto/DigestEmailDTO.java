package journals.dto;

import java.util.List;

import journals.model.User;
import journals.notification.api.EmailType;

public class DigestEmailDTO extends UserEmailDTO<List<JournalDTO>> {

  public DigestEmailDTO(User user, List<JournalDTO> journalList) {
    super(user, journalList, EmailType.DIGEST);
  }
}
