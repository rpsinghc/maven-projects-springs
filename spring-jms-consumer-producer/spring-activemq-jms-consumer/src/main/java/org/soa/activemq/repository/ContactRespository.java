package org.soa.activemq.repository;

import java.util.List;

import org.soa.activemq.model.Contact;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ContactRespository  extends MongoRepository<Contact, String>{
	public List<Contact> findByLastName(String lastName);
}
