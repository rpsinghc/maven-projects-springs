package org.soa.activemq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soa.activemq.service.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
public class ActiveQueueApplication implements CommandLineRunner {

	static Logger logger = LoggerFactory.getLogger(ActiveQueueApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ActiveQueueApplication.class, args);
	}

	@Autowired
	ConsumerService consumerService;

	@Override
	public void run(String... args) throws Exception {
		consumerService.readQueue();
	}
}