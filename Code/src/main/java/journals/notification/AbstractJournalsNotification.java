package journals.notification;

import javax.inject.Named;
import javax.jms.Queue;

import org.springframework.beans.factory.annotation.Autowired;

import journals.notification.api.EventType;

public abstract class AbstractJournalsNotification<T> extends AbstractNotification<EventType, T> {
  
  @Autowired
  @Named("journalsQueue")
  Queue queue;
  
  @Override
  protected Queue getQueue() {
    return queue;
  }
}
