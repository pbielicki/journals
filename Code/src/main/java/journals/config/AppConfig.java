package journals.config;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.Properties;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableJms
public class AppConfig {
  
  @Autowired
  Environment env;

  @Bean
  public JavaMailSender javaMailSender() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost(env.getProperty("java.mail.host", "localhost"));
    mailSender.setPort(env.getProperty("java.mail.port", Integer.class, 25));
    mailSender.setUsername(env.getProperty("java.mail.user"));
    mailSender.setPassword(env.getProperty("java.mail.pwd"));

    Properties props = new Properties();
    if (isNotBlank(mailSender.getUsername()) && isNotBlank(mailSender.getPassword())) {
      props.put("mail.smtp.auth", "true");
      props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    }
    
    props.put("mail.smtp.from", env.getRequiredProperty("java.mail.from"));
    mailSender.setJavaMailProperties(props);
    return mailSender;
  }
  
  @Bean
  public Queue journalsQueue() throws JMSException {
    return connectionFactory()
        .createConnection()
        .createSession(false, Session.CLIENT_ACKNOWLEDGE)
        .createQueue("queue.journals");
  }
  
  @Bean
  public Queue emailsQueue() throws JMSException {
    return connectionFactory()
        .createConnection()
        .createSession(false, Session.CLIENT_ACKNOWLEDGE)
        .createQueue("queue.emails");
  }
  
  @Bean
  public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
      DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
      factory.setConnectionFactory(connectionFactory());
      factory.setConcurrency("3-10");
      factory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
      factory.setSessionTransacted(false);
      return factory;
  }
  
  @Bean
  public ConnectionFactory connectionFactory() {
    final ActiveMQConnectionFactory cf;
    String url = env.getProperty("jms.broker.url");
    String user = env.getProperty("jms.broker.user");
    String pwd = env.getProperty("jms.broker.pwd");
    if (isNotBlank(url)) {
      if (isNotBlank(user) && isNotBlank(pwd)) {
        cf = new ActiveMQConnectionFactory(user, pwd, url);
      } else {
        cf= new ActiveMQConnectionFactory(url);
      }
    } else {
      cf = new ActiveMQConnectionFactory();
    }
    
    RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
    redeliveryPolicy.setBackOffMultiplier(5);
    redeliveryPolicy.setInitialRedeliveryDelay(10000L);
    redeliveryPolicy.setRedeliveryDelay(5000L);
    redeliveryPolicy.setUseExponentialBackOff(true);
    redeliveryPolicy.setMaximumRedeliveryDelay(30000L);
    cf.setRedeliveryPolicy(redeliveryPolicy);
    
    CachingConnectionFactory cachingCf = new CachingConnectionFactory(cf);
    cachingCf.setSessionCacheSize(20);
    
    return cachingCf;
  }
}
