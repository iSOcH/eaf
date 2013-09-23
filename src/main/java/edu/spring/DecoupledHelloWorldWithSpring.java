package edu.spring;

import edu.spring.domain.renderer.MessageRenderer;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * @author Michael Sacher
 */
public class DecoupledHelloWorldWithSpring {

  public static void main(String[] args) {
    BeanFactory factory = getBeanFactory();
    MessageRenderer mr = (MessageRenderer) factory.getBean("renderer");
    mr.render();
  }

  private static BeanFactory getBeanFactory() {
    XmlBeanFactory factory = new XmlBeanFactory(new ClassPathResource("/spring/spring-config.xml"));
    return factory;
  }
}
