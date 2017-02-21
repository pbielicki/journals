package journals.listener;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import journals.dto.JournalDTO;
import journals.dto.SingleJournalEmailDTO;
import journals.notification.api.EmailType;
import journals.notification.api.Notification;
import journals.repository.UserRepository;

@Service
@Profile("jms")
public class JournalPublishedListener {
  
  @Autowired
  UserRepository userRepository;

  @Autowired
  Notification<EmailType, SingleJournalEmailDTO> notification;
  
  @JmsListener(destination = "queue.journals", selector = "EventType = 'NEW_JOURNAL'")
  public void onMessage(TextMessage msg, Session session) throws JMSException {
    JournalDTO journal = new Gson().fromJson(msg.getText(), JournalDTO.class);
    userRepository.findBySubscriptionCategory(journal.getCategoryId())
      .forEach(user -> notification.notify(EmailType.SINGLE, new SingleJournalEmailDTO(user, journal)));
  }
}
