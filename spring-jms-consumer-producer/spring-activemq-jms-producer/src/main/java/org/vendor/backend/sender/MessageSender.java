package org.vendor.backend.sender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageSender {
	static Logger log = LoggerFactory.getLogger(MessageSender.class);
	
	@Autowired
	JmsTemplate jmsTemplate;
	
	@Value("${consumer.queue}")
	String destinationName;
	
	public void send(String json) {
			jmsTemplate.convertAndSend(destinationName, json);		
	}
	
}
