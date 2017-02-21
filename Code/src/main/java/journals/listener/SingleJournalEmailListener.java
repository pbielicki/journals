package journals.listener;

import java.lang.reflect.Type;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import journals.dto.SingleJournalEmailDTO;

@Service
@Profile("jms")
public class SingleJournalEmailListener extends AbstractEmailListener<SingleJournalEmailDTO> {
  
  @JmsListener(destination = "queue.emails", selector = "EmailType = 'SINGLE'")
  @Override
  protected void onMessage(TextMessage msg, Session session) throws JMSException {
    super.onMessage(msg, session);
  }
  
  @Override
  protected Type getType() {
    return SingleJournalEmailDTO.class;
  }
  
  @Override
  protected String getSubjectI18nKey() {
    return "email.single.subject";
  }

  @Override
  protected String getEmailTemplate() {
    return "email/single";
  }
}
