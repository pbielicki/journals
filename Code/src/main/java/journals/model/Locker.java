package journals.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Locker {

  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable = false, unique = true, updatable = false)
  private String name;
  
  @Column(nullable = false, updatable = false)
  private Date expiration;
  
  public Locker() {
  }
  
  public Locker(String name, Date expiration) {
    this.name = name;
    this.expiration = expiration;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
  
  public Date getExpiration() {
    return expiration;
  }
  
  public void setExpiration(Date expiration) {
    this.expiration = expiration;
  }
  
  @Override
  public String toString() {
    return "Lock = { name: '" + name + "', expiration: '" + expiration + "'}";
  }
}
