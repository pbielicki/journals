package journals.scheduled;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import journals.model.Locker;
import journals.repository.LockerRepository;

@Service
@Profile("scheduler")
public class LockerCleaner {

  @Autowired
  LockerRepository lockerRepository;

  private final static Logger LOG = Logger.getLogger(LockerCleaner.class);
  
  @Scheduled(cron = "${cleaner.cron}")
  public void checkForExpiredLocks() {
    List<Locker> expiredLocks = lockerRepository.findByExpirationBefore(new Date());
    if (expiredLocks.size() > 0) {
      LOG.info("Deleting expired locks: " + expiredLocks);
      try {
        lockerRepository.delete(expiredLocks);
      } catch (DataAccessException e) {
        LOG.info("Looks like someone else already removed the locker. Everything looks good.");
      }
    }
  }
}
