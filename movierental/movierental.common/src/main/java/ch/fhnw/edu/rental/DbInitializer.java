package ch.fhnw.edu.rental;

import org.springframework.context.ApplicationContext;

public interface DbInitializer {
	public void resetData(ApplicationContext context) throws Exception;
}
