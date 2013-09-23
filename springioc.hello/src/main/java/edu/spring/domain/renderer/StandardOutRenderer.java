package edu.spring.domain.renderer;

import edu.spring.domain.provider.MessageProvider;

/**
 * @author Michael Sacher
 */
public class StandardOutRenderer implements MessageRenderer {
  private MessageProvider messageProvider;

  @Override
  public void setMessageProvider(MessageProvider messageProvider) {
    this.messageProvider = messageProvider;
  }

  public MessageProvider getMessageProvider() {
    return messageProvider;
  }

  @Override
  public void render() {
    System.out.println(messageProvider.getMessage());
  }
}
