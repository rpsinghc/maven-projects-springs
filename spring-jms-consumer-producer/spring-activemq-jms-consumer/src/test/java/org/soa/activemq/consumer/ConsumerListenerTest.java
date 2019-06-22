package org.soa.activemq.consumer;

import javax.jms.Message;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soa.activemq.service.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConsumerListenerTest {
	Logger LOGGER = LoggerFactory.getLogger(ConsumerListenerTest.class);

	@Autowired
	ConsumerService consumerService;

	Message message;

	@Test
	public void onMessageTest() {
		Assert.assertNotEquals(null, message);
		consumerService.onMessage(message);
		
	}

}
