package com.example.demo;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.example.demo.sort.BinarySearchImpl;

@SpringBootApplication
@EnableScheduling
public class SpringExampleApplication {

	static Logger log = LoggerFactory.getLogger(SpringExampleApplication.class);
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	
	
	public static void main(String[] args) {
		ConfigurableApplicationContext cac = SpringApplication.run(SpringExampleApplication.class, args);
		BinarySearchImpl binarySearchImpl = cac.getBean(BinarySearchImpl.class);
		log.info(" {}",binarySearchImpl);
		log.info(" {}",binarySearchImpl.searchResult(new int[] {5,6,6,7}, 5));
	}
	
    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        log.info("The time is now {}", dateFormat.format(new Date()));
    }

}
