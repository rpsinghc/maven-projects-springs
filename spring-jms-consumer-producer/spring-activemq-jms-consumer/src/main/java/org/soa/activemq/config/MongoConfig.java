package org.soa.activemq.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

import com.mongodb.MongoClient;

//@Configuration
public class MongoConfig extends AbstractMongoConfiguration {

	@Value("${mongo.server}")
	String mongoServer;
	
	@Value("${mongo.port}")
	String port;
	
	@Value("${mongo.database}")
	String databaseName;
	
	@Override
	public MongoClient mongoClient() {
		return new MongoClient(mongoServer);
	}

	@Override
	protected String getDatabaseName() {
		return databaseName;
	}

}
