package journals.scheduled;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Named;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import journals.dto.DigestEmailDTO;
import journals.dto.JournalDTO;
import journals.model.Locker;
import journals.notification.api.EmailType;
import journals.notification.api.Notification;
import journals.repository.JournalRepository;
import journals.repository.LockerRepository;
import journals.repository.UserRepository;

@Service
@Profile("scheduler")
public class DailyJournalChecker {

  private final static Logger LOG = Logger.getLogger(DailyJournalChecker.class);
  
  @Autowired
  @Named("digestEmailNotification")
  Notification<EmailType, DigestEmailDTO> digestEmailNotification;
  
  @Autowired
  JournalRepository journalRepository;

  @Autowired
  UserRepository userRepository;
  
  @Autowired
  LockerRepository lockerRepository;
  
  long digestFrequency;
  
  @Value("${digest.frequency}")
  public void setDigestFrequency(String digestFrequency) {
    long seconds = 3600L; // number of seconds in an hour
    if (digestFrequency.endsWith("m")) {
      seconds = 60L;
    }

    if (Character.isAlphabetic(digestFrequency.charAt(digestFrequency.length() - 1))) {
      digestFrequency = digestFrequency.substring(0, digestFrequency.length() - 1);
    };
    
    this.digestFrequency = Long.parseLong(digestFrequency) * seconds * 1000L;
  }
  
  @Scheduled(cron = "${digest.cron}")
  public void checkForNewJournals() {
    Locker locker = new Locker(getClass().getName(), lockerExpiration());
    try {
      lockerRepository.save(locker);
    } catch (DataAccessException e) {
      LOG.info("Looks like another scheduler is running...");
      return;
    }
    
    List<JournalDTO> newJournals = journalRepository.findByPublishDateAfter(lastUpdateTime())
        .stream()
        .map(JournalDTO::new)
        .collect(Collectors.toList());

    if (newJournals.size() > 0) {
      userRepository.findAll()
        .forEach(user -> digestEmailNotification.notify(EmailType.DIGEST, new DigestEmailDTO(user, newJournals)));
    }
  }

  private Date lastUpdateTime() {
    return new Date(System.currentTimeMillis() - digestFrequency);
  }
  
  private Date lockerExpiration() {
    return new Date(System.currentTimeMillis() + (digestFrequency / 2L));
  }
}
