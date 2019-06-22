package org.soa.activemq.consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soa.activemq.adaptor.ConsumerAdaptor;
import org.soa.activemq.model.Contact;
import org.soa.activemq.repository.ContactRespository;
import org.soa.activemq.service.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConsumerAdapterTest {
	String json = "{\"vendorName\" : \"Microsoft1\", \"firstName\":\"Bob\",\"lastName\":\"Smith\",\"address\":\"123 Main St\",\"city\":\"Tulsa\",\"state\":\"OK\",\"zip\":\"71345\",\"email\":\"Bob@microsoft.com\",\"phoneNumber\":\"734-123-4567\"}";
	Logger LOGGER = LoggerFactory.getLogger(ConsumerListenerTest.class);
	
	@Autowired
	ConsumerAdaptor consumerAdaptor;
	
	@Autowired
	ContactRespository contactRepository;
	
	@Autowired
	ConsumerService consumerService;
	
	
	
	@Test
	public void sendToMongoTest() {
		ObjectMapper objectMapper = new ObjectMapper();
		Object object;
		try {
			object = objectMapper.readTree(json.getBytes());
			Contact contact = objectMapper.convertValue(object, Contact.class);
			consumerAdaptor.sendToMongo(contact);
			
			List<Contact> contactList =  contactRepository.findByLastName("Smith");
			
			LOGGER.info(contactList.toString());
			assertNotNull(json);
		} catch (IOException e) {
			assertThat(e);
		}
	}
	
	@Test
	public void getMessageTest() {
		consumerService.readQueue();
	}
	
	@After
	public void  tearDown() {
		System.out.println("down");
	}
}
