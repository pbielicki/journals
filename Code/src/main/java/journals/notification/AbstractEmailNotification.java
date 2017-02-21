package journals.notification;

import javax.inject.Named;
import javax.jms.Queue;

import org.springframework.beans.factory.annotation.Autowired;

import journals.notification.api.EmailType;

public abstract class AbstractEmailNotification<T> extends AbstractNotification<EmailType, T> {
  
  @Autowired
  @Named("emailsQueue")
  Queue queue;
  
  @Override
  protected Queue getQueue() {
    return queue;
  }
}
