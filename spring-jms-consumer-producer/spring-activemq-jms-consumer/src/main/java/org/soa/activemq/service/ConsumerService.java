package org.soa.activemq.service;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soa.activemq.adaptor.ConsumerAdaptor;
import org.soa.activemq.model.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ConsumerService {
	static Logger log = LoggerFactory.getLogger(ConsumerService.class);
	@Autowired
	JmsTemplate jmsTemplate;
	
	@Value("${rme.queue}")
	String rmeDestination;
	
	@Value("${consumer.queue}")
	String consumerQueue;
	
	@Autowired
	ConsumerAdaptor consumerAdaptor;
	
	@JmsListener(destination="${consumer.queue}", concurrency="4")
	public void onMessage(Message message) {
		saveToDB(message);
	}
	

	
	public void readQueue(){
		log.info("In read queue ...");
		Message message =  jmsTemplate.receive(consumerQueue);
		log.info("r1ead {}",message);
		while(message != null) {
			saveToDB(message);
			message =  jmsTemplate.receive(consumerQueue);
		}
	}
	
	
	private void saveToDB(Message message) {
		String json = null;
		System.out.println("In message");
		if (message instanceof TextMessage) {
			try {
				json =  ((TextMessage) message).getText();
				ObjectMapper objectMapper = new ObjectMapper();
				Contact contact = objectMapper.readValue(json.getBytes(), Contact.class);
				log.info("Send to mongo DB");
				consumerAdaptor.sendToMongo(contact);
			} catch (JMSException e) {
				log.info("Sending to RME Queue");
				jmsTemplate.convertAndSend(rmeDestination, json);
			} catch (JsonParseException e) {
				log.info("JSON error");
			} catch (JsonMappingException e) {
				log.info("JSON error");
			} catch (IOException e) {
				log.info("JSON error");
			}
		}
	}
}
