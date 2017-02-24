package journals.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;

@Configuration
public class TestConfig {

  @Bean
  public GreenMail getSmtpServer(Environment env) {
    return new GreenMail(new ServerSetup(
        env.getProperty("java.mail.port", Integer.class), 
        env.getProperty("java.mail.host"), 
        env.getProperty("java.mail.protocol"))
    );
  }
}
