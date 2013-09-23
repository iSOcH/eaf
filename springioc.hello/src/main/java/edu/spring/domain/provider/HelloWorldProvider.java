package edu.spring.domain.provider;

/**
 * @author Michael Sacher
 */
public class HelloWorldProvider implements MessageProvider {
  @Override
  public String getMessage(){
    return "hello world";
  }

}
