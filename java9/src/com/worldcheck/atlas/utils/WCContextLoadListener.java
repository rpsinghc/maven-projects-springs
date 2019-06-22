package com.worldcheck.atlas.utils;

import javax.servlet.ServletContextEvent;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class WCContextLoadListener extends ContextLoaderListener {
	public static final String DEFAULT_PROPERTIES_BEAN_NAME = "propertyConfigurer";
	public static final String DEFAULT_CONTEXT_PROPERTY = "configProperties";
	private String propertiesBeanName = "propertyConfigurer";
	private String contextProperty = "configProperties";

	public void contextInitialized(ServletContextEvent event) {
		WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
		ExposePropertyPlaceholderConfigurer configurer = (ExposePropertyPlaceholderConfigurer) wac
				.getBean(this.propertiesBeanName);
		event.getServletContext().setAttribute(this.contextProperty, configurer.getResolvedProps());
	}
}