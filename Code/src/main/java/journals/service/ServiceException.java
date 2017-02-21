package journals.service;

public class ServiceException extends RuntimeException {

  private static final long serialVersionUID = 3301354145767804447L;

  public ServiceException(String message) {
    super(message);
  }

  public ServiceException(String message, Throwable cause) {
    super(message, cause);
  }
}
