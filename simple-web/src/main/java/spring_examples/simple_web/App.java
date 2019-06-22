package spring_examples.simple_web;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages={
		"spring_examples.simple_web",
        "spring_examples.simple_web.controller"
    })
public class App  extends SpringBootServletInitializer{
	
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(App.class);
    }
    
	public static void main(String[] args) {
		ConfigurableApplicationContext ctx  = SpringApplication.run(App.class, args);
	       String[] beanNames = ctx.getBeanDefinitionNames();
	         
	        Arrays.sort(beanNames);
	 
	        for (String beanName : beanNames) {
	            System.out.println(beanName);
	        }
	}
}