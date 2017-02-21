package journals.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import journals.model.Locker;

public interface LockerRepository extends CrudRepository<Locker, Long> {

  List<Locker> findByExpirationBefore(Date expiration);
}
