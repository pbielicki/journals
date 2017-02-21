package journals.listener;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;

public class SingleJournalEmailListenerTest {

  private GreenMail smtpServer;

  @Before
  public void before() {
    smtpServer = new GreenMail(new ServerSetup(25, null, "smtp"));
    smtpServer.start();
  }

  @After
  public void after() {
    smtpServer.stop();
  }

  @Test
  public void testOnMessage() {
    fail("Not yet implemented");
  }

}
