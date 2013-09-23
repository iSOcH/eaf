package edu.spring.domain.renderer;

import edu.spring.domain.provider.MessageProvider;

/**
 * @author Michael Sacher
 */
public interface MessageRenderer {


  void render();

  void setMessageProvider(MessageProvider messageProvider);
}
