package edu.spring.domain.provider;

/**
 * @author Michael Sacher
 */
public class ExternalizedConstructorHelloworldMessageProvider implements MessageProvider {

  private String message;

  public ExternalizedConstructorHelloworldMessageProvider(String message) {
    this.message = message;
  }

  public ExternalizedConstructorHelloworldMessageProvider() {
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

}
