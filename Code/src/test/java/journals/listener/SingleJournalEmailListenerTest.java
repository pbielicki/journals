package journals.listener;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.collection.IsArrayWithSize.arrayWithSize;
import static org.hamcrest.text.StringContainsInOrder.stringContainsInOrder;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import javax.jms.Session;
import javax.jms.TextMessage;
import javax.mail.Address;
import javax.mail.Message.RecipientType;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import com.google.gson.Gson;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;

import journals.Application;
import journals.dto.JournalDTO;
import journals.dto.SingleJournalEmailDTO;
import journals.model.Category;
import journals.model.Journal;
import journals.model.User;

@SpringApplicationConfiguration(classes = Application.class)
@TestPropertySource(locations="classpath:test.properties")  
@ActiveProfiles("jms")
public class SingleJournalEmailListenerTest {
  
  @ClassRule
  public static final SpringClassRule SCR = new SpringClassRule();

  @Rule
  public SpringMethodRule springMethodRule = new SpringMethodRule();
    
  @Rule
  public MockitoRule rule = MockitoJUnit.rule();
  
  GreenMail smtpServer;
  
  @Autowired
  SingleJournalEmailListener mailListener;
  
  @Mock
  Session session;
  
  @Mock
  TextMessage message;

  @Mock
  User user;

  @Mock
  Journal journal;
  
  @Before
  public void before() {
    smtpServer = new GreenMail(new ServerSetup(8025, null, "smtp"));
    smtpServer.start();
  }

  @After
  public void after() {
    smtpServer.stop();
  }

  @Test
  public void testOnMessage() throws Exception {
    assertThat(mailListener, notNullValue());
    when(user.getEmail()).thenReturn("user@address.com");
    when(user.getLoginName()).thenReturn("john");
    when(journal.getName()).thenReturn("Journal name");
    when(journal.getCategory()).thenReturn(new Category());
    SingleJournalEmailDTO email = new SingleJournalEmailDTO(user, new JournalDTO(journal));
    when(message.getText()).thenReturn(new Gson().toJson(email));
    mailListener.onMessage(message, session);
    
    assertThat(smtpServer.getReceivedMessages(), arrayWithSize(1));
    assertThat(smtpServer.getReceivedMessages()[0].getContent(), notNullValue());
    //assertThat(smtpServer.getReceivedMessages()[0].getFrom(), notNullValue());
    Address[] toList = smtpServer.getReceivedMessages()[0].getRecipients(RecipientType.TO);
    assertThat(toList, notNullValue());
    assertThat(toList, arrayWithSize(1));
    //assertThat(smtpServer.getReceivedMessages()[0].getFrom(), is("przemyslaw.bielicki@gmail.com"));
    assertThat(toList[0].toString(), is("user@address.com"));
    String content = smtpServer.getReceivedMessages()[0].getContent().toString();
    assertThat(content, stringContainsInOrder(Arrays.asList("john", "Journal name")));
  }

}
