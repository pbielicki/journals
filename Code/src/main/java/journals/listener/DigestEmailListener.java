package journals.listener;

import java.lang.reflect.Type;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import journals.dto.DigestEmailDTO;

@Service
@Profile("jms")
public class DigestEmailListener extends AbstractEmailListener<DigestEmailDTO> {
  
  @JmsListener(destination = "queue.emails", selector = "EmailType = 'DIGEST'")
  @Override
  protected void onMessage(TextMessage msg, Session session) throws JMSException {
    super.onMessage(msg, session);
  }
  
  @Override
  protected Type getType() {
    return DigestEmailDTO.class;
  }
  
  @Override
  protected String getSubjectI18nKey() {
    return "email.digest.subject";
  }

  @Override
  protected String getEmailTemplate() {
    return "email/digest";
  }
}
