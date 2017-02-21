package journals.notification;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

import com.google.gson.Gson;

import journals.notification.api.Notification;

public abstract class AbstractNotification<E extends Enum<E>, T> implements Notification<E, T> {
  JmsTemplate jmsTemplate;
  
  @Autowired
  public void setConnectionFactory(ConnectionFactory cf) {
      jmsTemplate = new JmsTemplate(cf);
  }

  @Override
  public void notify(Enum<E> eventType, T event) {
    jmsTemplate.send(getQueue(), (session) -> {
          TextMessage msg = session.createTextMessage(convertEventToText(event));
          msg.setStringProperty(eventType.getClass().getSimpleName(), eventType.toString());
          return msg;
    });
  }
  
  protected String convertEventToText(T event) {
    return new Gson().toJson(event);
  }
  
  protected abstract Queue getQueue();
}
