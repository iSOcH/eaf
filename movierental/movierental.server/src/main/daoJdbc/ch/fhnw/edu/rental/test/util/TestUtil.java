package ch.fhnw.edu.rental.test.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class TestUtil {

	private static String[] DEFAULT_CONTEXT = {"business.xml", "datasource.xml"};
	
	static ApplicationContext context = null;
	
	public static ApplicationContext getSpringContext() throws Exception {
		return getSpringContext(DEFAULT_CONTEXT);
	}
	
	public static ApplicationContext getSpringContext(String[] contexts) throws Exception {
		context = new ClassPathXmlApplicationContext(contexts);
		JdbcDbInitializer dbinit = (JdbcDbInitializer)context.getBean("dbinit");
		dbinit.resetData(context);
		return context;
	}
	
	public static void setSupportForLazyLoading() {
	}

	public static void closeSupportForLazyLoading() {
	}

}
