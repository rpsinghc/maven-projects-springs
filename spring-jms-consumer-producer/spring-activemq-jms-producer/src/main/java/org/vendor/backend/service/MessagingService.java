package org.vendor.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.vendor.backend.sender.MessageSender;
import org.vendor.ui.model.Vendor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class MessagingService {
	static Logger log = LoggerFactory.getLogger(MessagingService.class);
	
	@Autowired
	MessageSender messsageSender;
	
	@Value("${consumer.queue}")
	String destinationName;
	
	public void process(Vendor contact) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String json = objectMapper.writeValueAsString(contact);
			messsageSender.send(json);
		} catch (JsonProcessingException e) {
			log.error("JSON error ", e);
		}
		
		
	}
	
}
