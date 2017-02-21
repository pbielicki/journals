package journals.listener;

import java.lang.reflect.Type;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.mail.internet.InternetAddress;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.google.gson.Gson;

import journals.dto.UserEmailDTO;

public abstract class AbstractEmailListener<T extends UserEmailDTO<?>> {
  
  private final static Logger LOG = Logger.getLogger(AbstractEmailListener.class);

  @Autowired
  TemplateEngine templateEngine;
  
  @Autowired
  JavaMailSender mailSender;
  
  @Autowired
  MessageSource i18n;
  
  protected void onMessage(TextMessage msg, Session session) throws JMSException {
    try {
      T userEmail = new Gson().fromJson(msg.getText(), getType());
      MimeMessagePreparator preparator = (mimeMessage) -> {
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
        message.setTo(new InternetAddress(userEmail.getEmail()));
        message.setSubject(i18n.getMessage(getSubjectI18nKey(), null, LocaleContextHolder.getLocale()));
        Context ctx = new Context();
        ctx.setVariable("user", userEmail.getLoginName());
        ctx.setVariable("content", userEmail.getContent());
        String body = templateEngine.process(getEmailTemplate(), ctx);
        message.setText(body, true);
      };

      mailSender.send(preparator);
      msg.acknowledge();
    } catch (JMSException | MailException e) {
      LOG.error("Unable to send email for NEW_JOURNAL message: " + e.getMessage());
      session.recover();
      session.close();
    } catch (Exception e) {
      LOG.error("Unable to process NEW_JOURNAL message", e);
    }
  }

  protected abstract String getSubjectI18nKey();
  protected abstract String getEmailTemplate();
  protected abstract Type getType();
}
