package org.soa.activemq.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class LoggingAOPConfig {
	static Logger logger = LoggerFactory.getLogger(LoggingAOPConfig.class);
	
	@Pointcut("execution (* org.soa.activemq.config.*.*(..))")
	public void contactRespositoryLog() {
		
	}
	
	@Before("execution (* org.soa.activemq.config.LoggingAOPConfig.contactRespositoryLog(..))")
	public void beforeLog(JoinPoint jp) {
		logger.info("Im in aspect {} {}",jp);
	}
}