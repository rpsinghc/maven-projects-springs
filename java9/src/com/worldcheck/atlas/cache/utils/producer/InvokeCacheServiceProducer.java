package com.worldcheck.atlas.cache.utils.producer;

import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Properties;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class InvokeCacheServiceProducer {
	private static Properties props = null;
	private static Context jndiContext = null;
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.cache.utils.producer.InvokeCacheServiceProducer");

	private Context getcontext(Properties props, String providerURL) throws NamingException {
		Context jndiContext = null;
		Hashtable<String, String> env = new Hashtable();
		env.put("java.naming.factory.initial", props.getProperty("atlas.pramati.factory.initial"));
		this.logger.debug("value of provider url is " + providerURL);
		env.put("java.naming.provider.url", providerURL);

		try {
			jndiContext = new InitialContext(env);
			this.logger.debug("jndiContext loaded sucessfully...");
			return jndiContext;
		} catch (NamingException var6) {
			this.logger.error("Naming exception occured " + var6);
			throw new NamingException();
		}
	}

	private Properties getproperties() throws IOException {
		Properties props = new Properties();
		InputStream is = ClassLoader.getSystemResourceAsStream("atlas.properties");

		try {
			if (is != null) {
				props.load(is);
				this.logger.debug("properties loaded sucessfully...");
			}

			return props;
		} catch (FileNotFoundException var4) {
			this.logger.error("Filenot found . Error is " + var4);
			throw new FileNotFoundException();
		} catch (IOException var5) {
			this.logger.error("IO exception occured " + var5);
			throw new IOException();
		}
	}

	private QueueConnection getconnection(Context jndiContext, Properties props) throws NamingException, JMSException {
		QueueConnection queueConnection = null;

		try {
			QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory) jndiContext
					.lookup(props.getProperty("QueueConnectionFactory"));
			queueConnection = queueConnectionFactory.createQueueConnection();
			this.logger.debug("QueueConnection operation is sucessfull...");
			return queueConnection;
		} catch (NamingException var6) {
			this.logger.error("Naming Exception occured " + var6);
			throw new NamingException();
		} catch (JMSException var7) {
			this.logger.error("JMS Exception occured " + var7);
			throw new JMSException(var7 + "");
		}
	}

	private Queue getqueue(Context jndiContext, Properties props) throws NamingException {
		Queue queue = null;

		try {
			queue = (Queue) jndiContext.lookup(props.getProperty("jms.processmanagerForCache"));
			this.logger.debug("the queue is " + queue);
			return queue;
		} catch (NamingException var5) {
			this.logger.error("Naming Exception occured " + var5);
			throw new NamingException();
		}
	}

	private void pushmessage(HashMap<String, Object> hashMap) throws NamingException, JMSException {
		this.logger.debug("Inside pushmessage method");
		if (jndiContext != null) {
			QueueConnection queueConnection = this.getconnection(jndiContext, props);
			QueueSession queueSession = null;

			try {
				if (queueConnection != null) {
					queueConnection.start();
					this.logger.debug("Queue connection started");
					queueSession = queueConnection.createQueueSession(false, 1);
					Queue queue = this.getqueue(jndiContext, props);
					this.logger.debug("After getting queue..");
					if (queue != null) {
						QueueSender queueSender = queueSession.createSender(queue);
						ObjectMessage objMessage = queueSession.createObjectMessage(hashMap);
						queueSender.send(objMessage, 2, 4, 0L);
						this.logger.debug("Message pushing successfull..");
					}
				}
			} catch (JMSException var14) {
				this.logger.error("JMS Exception occured " + var14);
				throw new JMSException("" + var14);
			} catch (NamingException var15) {
				this.logger.error("Naming Exception occured " + var15);
				throw new NamingException();
			} finally {
				try {
					if (queueSession != null) {
						queueSession.close();
					}

					if (queueConnection != null) {
						queueConnection.close();
					}
				} catch (JMSException var16) {
					throw new JMSException("" + var16);
				}

			}
		}

	}

	public void invokeService(HashMap<String, Object> hashMap, String ipaddress) throws IOException {
		this.logger.debug("ip address executed is " + ipaddress);
		props = this.getproperties();
		String addresses = props.getProperty("atlas.pramati.provider2.url");
		if (null != addresses && !"".equalsIgnoreCase(addresses)) {
			String[] nodesToNotify = addresses.split(",");

			for (int i = 0; i < nodesToNotify.length; ++i) {
				if (!nodesToNotify[i].contains(ipaddress)) {
					String nodeAddr = nodesToNotify[i];
					this.logger.debug("node address ");

					try {
						jndiContext = this.getcontext(props, nodeAddr);
						this.pushmessage(hashMap);
					} catch (Exception var8) {
						this.logger.error("Exception occured in invoke service method for node " + nodeAddr
								+ ". Error is " + var8);
					}
				}
			}
		}

	}
}