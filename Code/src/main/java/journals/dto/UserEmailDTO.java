package journals.dto;

import journals.model.User;
import journals.notification.api.EmailType;

public abstract class UserEmailDTO<T> {
  private EmailType emailType;
  
  private String loginName;
  
  private String email;
  
  private T content;

  public UserEmailDTO(User user, T content, EmailType emailType) {
    this.email = user.getEmail();
    this.loginName = user.getLoginName();
    this.content = content;
    this.emailType = emailType;
  }
  
  public EmailType getEmailType() {
    return emailType;
  }

  public void setEmailType(EmailType emailType) {
    this.emailType = emailType;
  }

  public String getLoginName() {
    return loginName;
  }

  public void setLoginName(String loginName) {
    this.loginName = loginName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public T getContent() {
    return content;
  }

  public void setContent(T content) {
    this.content = content;
  }
  
}
