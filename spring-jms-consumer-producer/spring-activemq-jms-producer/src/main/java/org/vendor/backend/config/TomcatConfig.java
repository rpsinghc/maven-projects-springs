package org.vendor.backend.config;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.catalina.Context;
import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jndi.JndiObjectFactoryBean;

@Configuration
public class TomcatConfig {
	
	@Bean
	public ServletWebServerFactory servletContainer() {
	    TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
	        @Override
	        protected void postProcessContext(Context context) {
	        	
	        	
	        	
                ContextResource resource = new ContextResource();

                resource.setType(DataSource.class.getName());
                resource.setName("j4s");
                resource.setProperty("factory", "org.apache.tomcat.jdbc.pool.DataSourceFactory");
                resource.setProperty("driverClassName", "com.mysql.jdbc.Driver");
                resource.setProperty("url", "jdbc:mysql://localhost/test");
                resource.setProperty("username", "java4s");
                resource.setProperty("password", "java4s");
                
                context.getNamingResources().addResource(resource);
	        }
	    };
	    //tomcat. .addAdditionalTomcatConnectors(redirectConnector());
	    return tomcat;
	}


	 
	    @Bean
	    public DataSource jndiDataSource() throws IllegalArgumentException, NamingException 
	    {
	        JndiObjectFactoryBean bean = new JndiObjectFactoryBean();    
	        bean.setJndiName("java:/comp/env/j4s");
	        bean.setProxyInterface(DataSource.class);
	        bean.setLookupOnStartup(false);
	        bean.afterPropertiesSet();
	        
	        return (DataSource) bean.getObject();
	    }
}