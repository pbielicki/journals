package journals.notification;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import journals.dto.JournalDTO;
import journals.model.Journal;

@Service
public class SingleJournalNotification extends AbstractJournalsNotification<Journal> {

  @Override
  protected String convertEventToText(Journal journal) {
    return new Gson().toJson(new JournalDTO(journal));
  }
}
