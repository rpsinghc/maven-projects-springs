package com.example.demo.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class LoggingConfigurationAspect {
	static Logger logger = LoggerFactory.getLogger(LoggingConfigurationAspect.class);
	
	
	@Before("execution (* com.example.demo.sort.*.*(..))")
	public void beforeLog(JoinPoint jp) {
		logger.info("Im in aspect {} {}",jp);
	}
}
