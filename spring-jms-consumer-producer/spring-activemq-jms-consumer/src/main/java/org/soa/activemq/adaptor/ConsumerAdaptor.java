package org.soa.activemq.adaptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soa.activemq.model.Contact;
import org.soa.activemq.repository.ContactRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
public class ConsumerAdaptor {

	static Logger log = LoggerFactory.getLogger(ConsumerAdaptor.class); 

	@Autowired
	ContactRespository contactRespository;
	
	@Transactional
	public void sendToMongo(Contact contact) {
		contactRespository.save(contact);
	}

}
